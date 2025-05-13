package it.getinsight.module.configuration.service;

import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.message.CoreMessageSource;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.enums.FileExtensionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.ERROR_EXPORT_CSV;
import static it.getinsight.message.MessageProperty.ERROR_IMPORT_CSV;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentConfigurationCsvParser {

    public List<AttachmentConfigurationDTO> parseToDTO(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(',')
                .setHeader()
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .get();

            Iterable<CSVRecord> records = format.parse(reader);
            List<AttachmentConfigurationDTO> list = new ArrayList<>();

            for (CSVRecord csvRecord : records) {
                convertToEntity(csvRecord).ifPresent(list::add);
            }

            return list;

        } catch (Exception e) {
            throw new BusinessException(CoreMessageSource.get().message(ERROR_IMPORT_CSV.key()), e);
        }
    }

    private Optional<AttachmentConfigurationDTO> convertToEntity(CSVRecord csvRecord) {
        try {
            if (csvRecord.get("key").isBlank()) {
                throw ERROR_IMPORT_CSV.businessException();
            }

            String key = csvRecord.get("key");
            String description = csvRecord.get("description");
            String icon = csvRecord.get("icon");
            Boolean required = Boolean.parseBoolean(csvRecord.get("required"));

            Set<FileExtensionType> allowedExtensions = Arrays.stream(csvRecord.get("allowedExtensions").split(","))
                .map(String::trim)
                .filter(ext -> !ext.isEmpty())
                .map(FileExtensionType::valueOf)
                .collect(Collectors.toSet());

            return Optional.of(
                AttachmentConfigurationDTO.builder()
                    .key(key)
                    .description(description)
                    .icon(icon)
                    .required(required)
                    .allowedExtensions(allowedExtensions)
                    .build()
            );
        } catch (Exception e) {
            log.warn("Erro ao processar linha do CSV: {}", csvRecord, e);
            return Optional.empty();
        }
    }

    public byte [] toCsv(List<AttachmentConfigurationEntity> configs) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                 .builder()
                 .setHeader("key", "description", "required", "allowedExtensions")
                 .setDelimiter(',')
                 .setQuote('\"')
                 .setQuoteMode(QuoteMode.ALL)
                 .setRecordSeparator("\n")
                 .get())
        ) {
            for (AttachmentConfigurationEntity config : configs) {
                csvPrinter.printRecord(
                    config.getKey(),
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
