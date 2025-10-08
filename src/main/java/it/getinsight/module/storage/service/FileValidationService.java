package it.getinsight.module.storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileValidationService {

    public void validateOwnerId(UUID ownerId) {
        if (ownerId == null) {
            throw OWNER_ID_REQUIRED_ERROR.businessException();
        }
    }

    public boolean shouldBePublic(String context, String fileType) {
        return "public-documents".equals(context) || "profile-images".equals(context);
    }

    public boolean shouldBeEphemeral(String context, String fileType) {
        return "temporary".equals(context) || "cache".equals(context);
    }
}
