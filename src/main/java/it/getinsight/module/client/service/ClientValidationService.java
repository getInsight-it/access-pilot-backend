package it.getinsight.module.client.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientValidationService {

    public void validateStatusTransition(ClientEntity entity, String newStatus) {
        log.debug("Validating status transition for client {} from {} to {}", 
                  entity.getClientId(), entity.getStatus(), newStatus);
        
        var validStatus = parseStatus(newStatus);
        
        if (validStatus.equals(entity.getStatus())) {
            throw CLIENT_ALREADY_HAS_STATUS.bind(newStatus).businessException();
        }
        
        if (BooleanUtils.isFalse(entity.getManaged()) && ClientStatus.PUBLISHED.equals(validStatus)) {
            throw CLIENT_UNMANAGED_CANNOT_BE_PUBLISHED.businessException();
        }
    }

    public void validateAttachmentConfigurations(List<AttachmentConfigurationEntity> configurations) {
        log.debug("Validating attachment configurations");
        
        Optional.ofNullable(configurations)
            .orElse(Collections.emptyList())
            .stream()
            .collect(Collectors.groupingBy(AttachmentConfigurationEntity::getName))
            .forEach((name, configList) -> {
                long activeCount = configList.stream()
                    .filter(AttachmentConfigurationEntity::getActive)
                    .count();
                    
                if (activeCount > 1) {
                    log.error("Multiple active configurations found for name: {}", name);
                    throw ATTACHMENTS_MULTIPLE_ACTIVE_ERROR.bind(name).businessException();
                }
            });
    }

    public void validateClientIdNotExists(String clientId, 
                                         it.getinsight.module.client.repository.ClientRepository clientRepository) {
        if (clientRepository.existsByClientId(clientId)) {
            log.error("Client with clientId {} already exists", clientId);
            throw CLIENT_ALREADY_EXISTS_ERROR.businessException();
        }
    }

    private ClientStatus parseStatus(String status) {
        return Arrays.stream(ClientStatus.values())
            .filter(s -> StringUtils.equalsIgnoreCase(s.name(), status))
            .findFirst()
            .orElseThrow(() -> {
                log.error("Invalid status provided: {}", status);
                return CLIENT_INVALID_STATUS.bind(status).businessException();
            });
    }
}

