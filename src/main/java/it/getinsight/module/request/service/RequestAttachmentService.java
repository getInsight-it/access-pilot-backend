package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestAttachmentDTO;
import it.getinsight.module.request.entity.RequestAttachmentEntity;
import it.getinsight.module.request.mapper.RequestAttachmentMapper;
import it.getinsight.module.request.repository.RequestAttachmentFileRepository;
import it.getinsight.module.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class RequestAttachmentService {

    private final RequestAttachmentFileRepository requestAttachmentRepository;
    private final RequestRepository requestRepository;
    private final RequestAttachmentMapper requestAttachmentMapper;

    public List<RequestAttachmentDTO> findAllByRequest(Long requestId) {
        var request = requestRepository.findById(requestId).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        return requestAttachmentMapper.toDto(requestAttachmentRepository.findAllByRequest(request));
    }

}
