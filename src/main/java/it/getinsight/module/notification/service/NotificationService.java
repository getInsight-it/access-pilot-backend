package it.getinsight.module.notification.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.notification.dto.Notification;
import it.getinsight.module.notification.dto.NotificationFilterDTO;
import it.getinsight.module.notification.dto.NotificationSummaryDTO;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.notification.mapper.NotificationMapper;
import it.getinsight.module.notification.repository.NotificationRepository;
import it.getinsight.module.web_notification.service.WebNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static it.getinsight.message.MessageProperty.NOTIFICATION_NOT_FOUND_ERROR;
import static it.getinsight.message.MessageProperty.REQUIRED_FIELD_WITH_PARAMETER;


@Service
@Slf4j
public class NotificationService {

    private final NotificationSender notificationSender;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;
    private final WebNotificationService webNotificationService;
    private final NotificationRepository notificationRepository;

    public NotificationService(@Qualifier("notificationSenderImpl") NotificationSender notificationSender,
                               NotificationMapper notificationMapper,
                               EmailService emailService,
                               WebNotificationService webNotificationService, NotificationRepository notificationRepository) {
        this.notificationSender = notificationSender;
        this.notificationMapper = notificationMapper;
        this.emailService = emailService;
        this.webNotificationService = webNotificationService;
        this.notificationRepository = notificationRepository;
    }

    public Notification send(Notification notification) {
        return notificationSender.send(notification);
    }

    public PageableResponseModel<Notification> getNotifications(PageableRequestModel<NotificationFilterDTO> configPage) {
        NotificationFilterDTO filter = configPage.getFilter().orElseThrow(() -> REQUIRED_FIELD_WITH_PARAMETER.bind("type").businessException());
        if (Objects.isNull(filter.type())) {
            throw REQUIRED_FIELD_WITH_PARAMETER.bind("type").businessException();
        }

        Map<NotificationType, Function<NotificationFilterDTO, PageableResponseModel<Notification>>> notificationHandlers = Map.of(
            NotificationType.EMAIL, filterDTO -> handleEmailNotifications(filterDTO, configPage),
            NotificationType.WEB, filterDTO -> handleWebNotifications(filterDTO, configPage)
        );

        NotificationType type = filter.type();
        Function<NotificationFilterDTO, PageableResponseModel<Notification>> handler = notificationHandlers.get(type);

        if (handler == null) {
            throw REQUIRED_FIELD_WITH_PARAMETER.bind("type").businessException();
        }

        return handler.apply(filter);
    }

    private PageableResponseModel<Notification> handleEmailNotifications(NotificationFilterDTO filterDTO, PageableRequestModel<NotificationFilterDTO> configPage) {
        var emailFilterDTO = notificationMapper.toEmailFilterDTO(filterDTO);
        var configPageEmail = PageableRequestModel.of(configPage.getPageNumber(), configPage.getPageSize(), configPage.getSortType(), configPage.getSortField(), emailFilterDTO);
        var page = emailService.getNotificationsFull(configPageEmail);
        return PaginationHelper.toPageResponse(page.getItems().stream().map(o -> (Notification) o).toList(), page.getTotal());
    }

    private PageableResponseModel<Notification> handleWebNotifications(NotificationFilterDTO filterDTO, PageableRequestModel<NotificationFilterDTO> configPage) {
        var webFilterDTO = notificationMapper.toWebNotificationDTO(filterDTO);
        var configPageWeb = PageableRequestModel.of(configPage.getPageNumber(), configPage.getPageSize(), configPage.getSortType(), configPage.getSortField(), webFilterDTO);
        var page = webNotificationService.getNotificationsFull(configPageWeb);
        return PaginationHelper.toPageResponse(page.getItems().stream().map(o -> (Notification) o).toList(), page.getTotal());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOpenNotification(Long id, Boolean read) {
        var notification = notificationRepository.findById(id).orElseThrow(NOTIFICATION_NOT_FOUND_ERROR::resourceNotFoundException);
        notification.setIsOpened(read);
        notificationRepository.save(notification);
    }

    public NotificationSummaryDTO getNotificationsSummary(String externalId, NotificationType type) {
        long totalRead = notificationRepository.countByUser_ExternalIdAndIsOpenedAndType(externalId, true, type);
        long totalUnread = notificationRepository.countByUser_ExternalIdAndIsOpenedAndType(externalId, false, type);
        return NotificationSummaryDTO.builder()
            .totalRead(totalRead)
            .totalUnread(totalUnread)
            .total(totalRead + totalUnread)
            .build();
    }
}
