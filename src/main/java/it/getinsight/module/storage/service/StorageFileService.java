package it.getinsight.module.storage.service;


import it.getinsight.core.exception.InfraException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.storage.dto.StorageFileDTO;
import it.getinsight.module.storage.dto.StorageFileFilterDTO;
import it.getinsight.module.storage.entity.StorageFileEntity;
import it.getinsight.module.storage.mapper.StorageFileFilterMapper;
import it.getinsight.module.storage.mapper.StorageFileMapper;
import it.getinsight.module.storage.repository.StorageFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
public class StorageFileService {

    private final StorageFileRepository storageRepository;
    private final StorageService storageService;
    private final StorageFileMapper storageFileMapper;
    private final StorageFileFilterMapper storageFileFilterMapper;
    private final StoragePolicyService storagePolicyService;

    public StorageFileDTO findById(Long id) {
        return storageFileMapper.toDto(storageRepository.findById(id).orElseThrow(FILE_NOT_FOUND_ERROR::businessException));
    }

    public void delete(Long id) {
        storageRepository.deleteById(id);
    }


    public PageableResponseModel<StorageFileDTO> getFilesPaginated(PageableRequestModel<StorageFileFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(storageFileFilterMapper::toDto)
            .map(storageFileMapper::toEntity)
            .orElse(new StorageFileEntity());

        final var matcher = ExampleMatcher
            .matchingAny()
            .withIgnoreNullValues()
            .withMatcher("originalFilename", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("ownerId", ExampleMatcher.GenericPropertyMatcher::exact);

            final var example = Example.of(model, matcher);
            final var page = storageRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(storageFileMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public StorageFileDTO findByName(String name) {
        return storageFileMapper.toDto(storageRepository.findByOriginalFilename(name)
            .orElseThrow(FILE_NOT_FOUND_ERROR::businessException));
    }

    @Transactional
    public List<StorageFileEntity> saveAll(List<MultipartFile> attachments, String bucket, Boolean isPublic, Boolean ephemeral, UUID ownerId) {
        var files = new ArrayList<StorageFileEntity>();
        if (attachments != null && !attachments.isEmpty()){
            attachments.forEach(file -> {
                try {
                    var actualBucket = bucket != null ? bucket : storagePolicyService.getDefaultPrivateBucket();
                    var actualIsPublic = isPublic != null ? isPublic : storagePolicyService.shouldBePublic("default", file.getContentType());
                    var actualEphemeral = ephemeral != null ? ephemeral : storagePolicyService.shouldBeEphemeral("default", file.getContentType());
                    
                    var fileUploaded = upload(actualBucket, actualIsPublic, actualEphemeral, ownerId, file.getOriginalFilename(),
                        file.getContentType(), file.getSize(), file.getInputStream());
                    files.add(fileUploaded);
                } catch (IOException e) {
                    throw FILE_SAVE_ERROR.infraException();
                }
            });
        }

        return files;
    }


    public StorageFileEntity upload(String bucket,
                                    Boolean isPublic,
                                    Boolean ephemeral,
                                    UUID ownerId,
                                    String originalFilename,
                                    String contentType,
                                    Long size,
                                    InputStream inputStream) {
        storagePolicyService.validateOwnerId(ownerId);

        var entity = StorageFileEntity.builder()
            .bucket(bucket)
            .excluded(false)
            .isPublic(isPublic)
            .ephemeral(ephemeral)
            .ownerId(ownerId)
            .downloadCount(0L)
            .originalFilename(originalFilename)
            .mimeType(contentType)
            .filesize(size)
            .fileId(UUID.randomUUID())
            .build();

        entity = storageRepository.save(entity);
        storageService.upload(bucket, isPublic, ephemeral, ownerId.toString(), originalFilename, contentType, size, inputStream);
        return entity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public InputStreamResource download(Long fileId, boolean registerDownload) {
        var storageFileEntity = storageRepository.findById(fileId)
            .orElseThrow(FILE_NOT_FOUND_ERROR::businessException);
        var fileInputStream = storageService.download(storageFileEntity.getBucket(), storageFileEntity.getOriginalFilename());
        var file = new InputStreamResource(fileInputStream);

        if (registerDownload) {
            registerDownloadEvent(storageFileEntity);
        }
        return file;
    }

    private void registerDownloadEvent(StorageFileEntity storageFileEntity) {
        storageFileEntity.setDownloadCount(storageFileEntity.getDownloadCount() + 1);
        storageRepository.save(storageFileEntity);
    }
}
