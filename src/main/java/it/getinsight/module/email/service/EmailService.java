package it.getinsight.module.email.service;


import it.getinsight.core.exception.InfraException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.entity.EmailSentEntity;
import it.getinsight.module.email.entity.EmailStatus;
import it.getinsight.module.email.mapper.EmailMapper;
import it.getinsight.module.email.repository.EmailRepository;
import it.getinsight.module.storage.service.StorageFileService;
import it.getinsight.module.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.apache.commons.lang3.BooleanUtils.isTrue;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;
    private final EmailMapper emailMapper;
    private final StorageFileService storageFileService;

    public static final String  PRIVATE_GETINSIGHT_ACCESSPILOT_EMAILS_BUCKET = "private-getinsight-accesspilot-emails";

    @Value("${spring.mail.properties.mail.from}")
    private String emailFrom;

     public void sendMail(EmailDTO emailDTO){
         final var content = isTrue(emailDTO.isHtml()) ? processContentByTemplate(emailDTO.templateName(), emailDTO.variables()) : emailDTO.content();
         final var userEntity = userRepository.findById(emailDTO.userId()).orElseThrow(ResourceNotFoundException::new);
         final var emailSent = EmailSentEntity.builder()
                 .to(emailDTO.to())
                 .from(emailFrom)
                 .subject(emailDTO.subject())
                 .isHtml(emailDTO.isHtml())
                 .user(userEntity)
                 .content(content)
                 .build();
         sendEmail(emailSent);
     }

      private void sendEmail(EmailSentEntity emailSentEntity) {
        Map<String, File> attachments = null;
        try{
            var mimeMessageMapPair = makeEmail(emailSentEntity);
            var message = mimeMessageMapPair.getLeft();
            attachments = mimeMessageMapPair.getRight();
            log.info("Sending email: {} - {}", emailSentEntity.getTo(), emailSentEntity.getSubject());
            emailSender.send(message);

            emailSentEntity.setSuccess(true);
            emailSentEntity.setStatus(EmailStatus.SENT);
            emailRepository.save(emailSentEntity);
            final var content = emailSentEntity.getContent();
            storageFileService.upload(PRIVATE_GETINSIGHT_ACCESSPILOT_EMAILS_BUCKET,
                false,
                false,
                emailSentEntity.getUuid(), "%s.html".formatted(emailSentEntity.getUuid()), "text/html", (long) content.getBytes().length, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            log.info("EmailDTO sent successfully: {} - {}", emailSentEntity.getTo(), emailSentEntity.getSubject());
        }catch (Exception e){
            handleEmailError(e, emailSentEntity);
        }finally {
            cleanupAttachments(attachments);
        }

    }

    private void handleEmailError(Exception e, EmailSentEntity emailSentEntity) {
        emailSentEntity.setSuccess(false);
        emailRepository.save(emailSentEntity);
        log.info("EmailDTO send error: {} - {}", emailSentEntity.getTo(), emailSentEntity.getSubject(), e);
        throw new InfraException("sent.email.error");
    }

    private void cleanupAttachments(Map<String, File> attachments) {
        if (attachments != null) {
            attachments.values().forEach(File::delete);
        }
    }


    private Pair<MimeMessage, Map<String, File>> makeEmail(EmailSentEntity emailSentEntity) throws MessagingException {
        var mimeMessage = emailSender.createMimeMessage();
        var helper = createMimeMessageHelper(mimeMessage);

        configureMimeMessageHelper(helper, emailSentEntity);

        var attachments = new HashMap<String, File>();

        return Pair.of(mimeMessage, attachments);
    }

    private MimeMessageHelper createMimeMessageHelper(MimeMessage mimeMessage) throws MessagingException {
        return new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
    }

    private void configureMimeMessageHelper(MimeMessageHelper helper, EmailSentEntity emailSentEntity) throws MessagingException {
        helper.setTo(emailSentEntity.getTo().split(";"));
        helper.setFrom(new InternetAddress(emailSentEntity.getFrom()));
        helper.setSubject(emailSentEntity.getSubject());
        helper.setText(emailSentEntity.getContent(), emailSentEntity.getIsHtml());

        if(emailSentEntity.getCopy() != null && !emailSentEntity.getCopy().isEmpty()){
            helper.setCc(emailSentEntity.getCopy().split(";"));
        }

        if(emailSentEntity.getAnonymousCopy() != null && !emailSentEntity.getAnonymousCopy().isEmpty()){
            helper.setBcc(emailSentEntity.getAnonymousCopy().split(";"));
        }
    }

    private String processContentByTemplate(final String templateName, final Map<String, Object> variables){
        try {
            var context = new Context(Locale.getDefault(), variables);
            return templateEngine.process(templateName, context);
        } catch (Exception e) {
            throw new InfraException("build.template.error");
        }
    }

    public PageableResponseModel<EmailDTO> getNotifications(PageableRequestModel<EmailDTO> configPage) {
        final var page = emailRepository.findAll(PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(emailMapper.toDto(page.getContent()), page.getTotalElements());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEmail(Long emailId, EmailDTO emailDTO) {
        final var emailSent = emailRepository.findById(emailId).orElseThrow(ResourceNotFoundException::new);
        emailMapper.fromDto(emailDTO, emailSent);
        emailRepository.save(emailSent);
    }


    public EmailDTO getEmailById(Long emailId) {
        final var emailSent = emailRepository.findById(emailId).orElseThrow(ResourceNotFoundException::new);
        return emailMapper.toDto(emailSent);
    }

}
