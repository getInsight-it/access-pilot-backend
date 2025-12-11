package it.getinsight.core.exception;

import it.getinsight.core.message.CoreMessageSource;
import it.getinsight.core.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Collections;
import java.util.List;

import static it.getinsight.message.MessageProperty.FILE_UPLOAD_SIZE_EXCEEDED;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        String errorMessage = CoreMessageSource.get().message(FILE_UPLOAD_SIZE_EXCEEDED.key());

        List<ErrorMessage> errors = Collections.singletonList(
            new ErrorMessage(
                FILE_UPLOAD_SIZE_EXCEEDED.key(),
                errorMessage
            )
        );

        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(errors);
    }

}
