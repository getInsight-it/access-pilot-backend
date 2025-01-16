package it.getinsight.module.erro.service;

import it.getinsight.core.exception.*;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.EXAMPLE_ERROR_MESSAGE;
import static it.getinsight.message.MessageProperty.EXAMPLE_MESSAGE_WITH_PARAMETER;

@Service
public class ErrorService {

    /**
     * When the logged-in user does not have access to the resource
     */
    public void failWithAccessForbiddenException() {
        throw new AccessForbiddenException("AccessForbiddenException");
    }

    /**
     * Do not use this way! Thus, the frontend does not receive an error message!
     */
    public void failWithBusinessExceptionEmpty() {
        throw new BusinessException();
    }

    /**
     * When a business rule violation occurs with a message
     */
    public void failWithBusinessException() {
        throw EXAMPLE_ERROR_MESSAGE.businessException();
    }

    /**
     * When a business rule violation occurs with a parameterized message
     */
    public void failWithBusinessExceptionMessage(String parameter) {
        throw EXAMPLE_MESSAGE_WITH_PARAMETER
            .bind(parameter)
            .businessException();
    }

    /**
     * When an error occurs in the FeignClient when invoking an external service
     */
    public void failWithFeignIntegrationException() {
        throw new FeignIntegrationException();
    }

    /**
     * When the requested resource is not found
     */
    public void failWithResourceNotFoundException() {
        throw new ResourceNotFoundException();
    }

    /**
     * When a security validation is violated (different from {@link ErrorService#failWithAccessForbiddenException()}
     */
    public void failWithSecurityValidationException() {
        throw new SecurityValidationException();
    }

    /**
     * When an infrastructure error occurs with a message
     */
    public void failWithInfraException() {
        throw EXAMPLE_ERROR_MESSAGE.infraException();
    }

    /**
     * When an infrastructure error occurs with a parameterized message
     */
    public void failWithInfraExceptionMessage(String parameter) {
        throw EXAMPLE_MESSAGE_WITH_PARAMETER
            .bind(parameter)
            .infraException();
    }
}
