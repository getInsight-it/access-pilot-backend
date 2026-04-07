package it.getinsight.module.configuration.service;

import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.enums.FileExtensionType;
import it.getinsight.module.shared.service.ColorPaletteService;
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
import static it.getinsight.utilitario.PropertyPathConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentConfigurationCsvParser {

    private final ColorPaletteService colorPaletteService;

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
            throw ERROR_IMPORT_CSV.businessException();
        }
    }

    private Optional<AttachmentConfigurationDTO> convertToEntity(CSVRecord csvRecord) {
        try {
            String name = csvRecord.get(AttachmentConfigurationCSV.NAME).trim();
            if (name.isBlank()) {
                throw ERROR_IMPORT_CSV.businessException();
            }

            String description = csvRecord.isSet(AttachmentConfigurationCSV.DESCRIPTION) ? csvRecord.get(AttachmentConfigurationCSV.DESCRIPTION) : null;
            String icon = csvRecord.isSet(AttachmentConfigurationCSV.ICON) ? csvRecord.get(AttachmentConfigurationCSV.ICON) : null;
            String color = csvRecord.isSet(AttachmentConfigurationCSV.COLOR) ? csvRecord.get(AttachmentConfigurationCSV.COLOR) : null;
            Boolean required = Boolean.parseBoolean(csvRecord.get(AttachmentConfigurationCSV.REQUIRED));
            String allowedExtensionsRaw = csvRecord.get(AttachmentConfigurationCSV.ALLOWED_EXTENSIONS);

            Set<FileExtensionType> allowedExtensions = Arrays.stream(allowedExtensionsRaw.split(","))
                .map(String::trim)
                .filter(ext -> !ext.isEmpty())
                .map(FileExtensionType::valueOf)
                .collect(Collectors.toSet());

            return Optional.of(
                AttachmentConfigurationDTO.builder()
                    .name(name)
                    .description(description)
                    .icon(icon)
                    .color(colorPaletteService.parseFromHex(color))
                    .required(required)
                    .allowedExtensions(allowedExtensions)
                    .build()
            );
        } catch (Exception e) {
            log.warn("Error parsing csv record: {}", csvRecord, e);
            return Optional.empty();
        }
    }

    public byte [] toCsv(List<AttachmentConfigurationEntity> configs) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                 .builder()
                 .setHeader(
                     AttachmentConfigurationCSV.NAME,
                     AttachmentConfigurationCSV.DESCRIPTION,
                     AttachmentConfigurationCSV.REQUIRED,
                     AttachmentConfigurationCSV.ALLOWED_EXTENSIONS,
                     AttachmentConfigurationCSV.ICON,
                     AttachmentConfigurationCSV.COLOR
                 )
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
                        : "",
                    config.getIcon(),
                    config.getColor() != null ? config.getColor().hex() : null
                );
            }
            csvPrinter.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw ERROR_EXPORT_CSV.businessException();
        }
    }

    public byte[] toCsvFromDto(List<AttachmentConfigurationDTO> configs) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                 .builder()
                 .setHeader(
                     AttachmentConfigurationCSV.NAME,
                     AttachmentConfigurationCSV.DESCRIPTION,
                     AttachmentConfigurationCSV.REQUIRED,
                     AttachmentConfigurationCSV.ALLOWED_EXTENSIONS,
                     AttachmentConfigurationCSV.ICON,
                     AttachmentConfigurationCSV.COLOR
                 )
                 .setDelimiter(',')
                 .setQuote('\"')
                 .setQuoteMode(QuoteMode.ALL)
                 .setRecordSeparator("\n")
                 .get())
        ) {
            for (AttachmentConfigurationDTO config : configs) {
                csvPrinter.printRecord(
                    config.name(),
                    config.description(),
                    config.required() != null ? config.required() : "",
                    config.allowedExtensions() != null
                        ? config.allowedExtensions().stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(","))
                        : "",
                    config.icon(),
                    config.color() != null ? config.color().hex() : null
                );
            }
            csvPrinter.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw ERROR_EXPORT_CSV.businessException();
        }
    }
}
