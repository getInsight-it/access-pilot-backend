package it.getinsight.module.configuration.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.mapper.AttachmentConfigurationMapper;
import it.getinsight.module.configuration.repository.AttachmentConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.ERROR_CONFIGURATION_NOT_FOUND;
import static it.getinsight.message.MessageProperty.ERROR_EXPORT_CSV;


@Service
@RequiredArgsConstructor
public class AttachmentConfigurationService {

    private final AttachmentConfigurationRepository attachmentConfigurationRepository;
    private final AttachmentConfigurationMapper attachmentConfigurationMapper;
    private final AttachmentConfigurationCsvParser attachmentConfigurationCsvParser;

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        var configurationEntity = attachmentConfigurationRepository.findById(id).orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException);
        attachmentConfigurationRepository.softDelete(configurationEntity.getId());
    }

    public AttachmentConfigurationDTO findById(Long id) {
        var configurationEntity = attachmentConfigurationRepository.findById(id).orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException);
        return attachmentConfigurationMapper.toDto(configurationEntity);
    }


    public void importAttachmentConfiguration(ClientEntity client, MultipartFile file) {
        var configs = attachmentConfigurationCsvParser.parse(client, file);
        attachmentConfigurationRepository.saveAll(configs);
    }


    public byte[] toCsv(List<AttachmentConfigurationEntity> configs) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                 .builder()
                 .setHeader("name", "description", "required", "allowedExtensions")
                 .setDelimiter(',')
                 .setQuote('\"')
                 .setQuoteMode(QuoteMode.ALL)
                 .setRecordSeparator("\n")
                 .get())
        ) {
            for (AttachmentConfigurationEntity config : configs) {
                csvPrinter.printRecord(
                    config.getName(),
                    config.getDescription(),
                    config.getRequired(),
                    config.getAllowedExtensions() != null
                        ? config.getAllowedExtensions().stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(","))
                        : ""
                );
            }
            csvPrinter.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw ERROR_EXPORT_CSV.businessException();
        }
    }

}
