package it.getinsight.module.erro.service;

import it.getinsight.core.exception.*;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.EXAMPLE_ERROR_MESSAGE;
import static it.getinsight.message.MessageProperty.EXAMPLE_MESSAGE_WITH_PARAMETER;

@Service
public class ErrorService {


    public void failWithAccessForbiddenException() {
        throw new AccessForbiddenException("AccessForbiddenException");
    }


    public void failWithBusinessExceptionEmpty() {
        throw new BusinessException();
    }


    public void failWithBusinessException() {
        throw EXAMPLE_ERROR_MESSAGE.businessException();
    }


    public void failWithBusinessExceptionMessage(String parameter) {
        throw EXAMPLE_MESSAGE_WITH_PARAMETER
            .bind(parameter)
            .businessException();
    }


    public void failWithFeignIntegrationException() {
        throw new FeignIntegrationException();
    }


    public void failWithResourceNotFoundException() {
        throw new ResourceNotFoundException();
    }


    public void failWithSecurityValidationException() {
        throw new SecurityValidationException();
    }


    public void failWithInfraException() {
        throw EXAMPLE_ERROR_MESSAGE.infraException();
    }


    public void failWithInfraExceptionMessage(String parameter) {
        throw EXAMPLE_MESSAGE_WITH_PARAMETER
            .bind(parameter)
            .infraException();
    }
}
