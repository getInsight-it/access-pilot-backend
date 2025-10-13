package it.getinsight.module.request.service;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.request.entity.RequestAttachmentEntity;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.RequestAttachmentFileRepository;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.storage.entity.StorageFileEntity;
import it.getinsight.module.storage.service.StorageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestAttachmentService {

    private final RequestAttachmentFileRepository requestAttachmentRepository;
    private final RequestRepository requestRepository;
    private final StorageFileService storageFileService;

    private static final String PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "private-getinsight-accesspilot-docs";


    public List<RequestAttachmentEntity> findAllByRequest(Long requestId) {
        log.debug("Finding all attachments for request: {}", requestId);
        RequestEntity request = requestRepository.findById(requestId)
            .orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        return requestAttachmentRepository.findAllByRequest(request);
    }


    @Transactional
    public void saveRequestFiles(MultiValueMap<String, MultipartFile> attachments,
                                List<AttachmentConfigurationEntity> configurations,
                                RequestEntity request) {

        for (AttachmentConfigurationEntity config : configurations) {
            List<MultipartFile> files = attachments.get(config.getKey());
            if (files == null || files.isEmpty()) {
                log.warn("No attachments found for configuration key {}", config.getKey());
                continue;
            }

            var storageFileEntities = storageFileService.saveAll(
                files,
                PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET,
                false,
                false,
                request.getUuid()
            );

            for (StorageFileEntity storageFileEntity : storageFileEntities) {
                var requestFile = RequestAttachmentEntity.builder()
                    .request(request)
                    .file(storageFileEntity)
                    .configuration(config)
                    .uuid(UUID.randomUUID())
                    .active(true)
                    .build();
                requestAttachmentRepository.save(requestFile);
            }

            log.info("Saved {} files for request {}", storageFileEntities.size(), request.getUuid());
        }
    }
}
