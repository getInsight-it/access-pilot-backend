package it.getinsight.module.storage.service;


import it.getinsight.core.exception.InfraException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.storage.dto.StorageFileDTO;
import it.getinsight.module.storage.entity.StorageFileEntity;
import it.getinsight.module.storage.mapper.StorageFileMapper;
import it.getinsight.module.storage.provider.StorageProvider;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageFileService {

    private final StorageFileRepository storageRepository;
    private final RequestRepository requestRepository;
    private final StorageProvider storageProvider;
    private final StorageFileMapper storageFileMapper;

    public StorageFileDTO findById(Long id) {
        var storageFileEntity = storageRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return storageFileMapper.toDto(storageFileEntity);
    }

    public void delete(Long id) {
        storageRepository.deleteById(id);
    }


    public PageableResponseModel<StorageFileDTO> getFilesPaginated(PageableRequestModel<String> configPage) {
        final var model = configPage
            .getFilter()
            .map(o -> StorageFileEntity.builder().originalFilename(o).build())
            .orElse(new StorageFileEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("originalFilename", ExampleMatcher.GenericPropertyMatcher::contains);

            final var example = Example.of(model, matcher);
            final var page = storageRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(storageFileMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public StorageFileDTO findByName(String name) {
        return storageFileMapper.toDto(storageRepository.findByOriginalFilename(name)
            .orElseThrow(() -> new InfraException("Arquivo não encontrado")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(List<MultipartFile> attachments, String bucket, Boolean isPublic, Boolean ephemeral, Long requestId) {
        for (MultipartFile file : attachments) {
            try {
                var requestEntity = requestId != null ? requestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found")) : null;
                var storageFileEntity = StorageFileEntity.builder()
                    .bucket(bucket)
                    .excluded(false)
                    .isPublic(isPublic)
                    .ephemeral(ephemeral)
                    .request(requestEntity)
                    .downloadCount(0L)
                    .originalFilename(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .filesize(file.getSize())
                    .fileId(UUID.randomUUID())
                    .build();

                storageFileEntity = storageRepository.save(storageFileEntity);

                storageProvider.uploadFile(storageFileEntity, file.getInputStream());
            } catch (IOException e) {
                throw new InfraException("Erro ao salvar arquivo");
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public InputStreamResource download(Long fileId, boolean registerDownload) {
        var storageFileEntity = storageRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado"));
        var file = storageProvider.download(storageFileEntity);

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
