package it.getinsight.module.storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoragePolicyService {

    private final BucketPolicyService bucketPolicyService;
    private final FileValidationService fileValidationService;

    public String determineBucket(String fileType, boolean isPublic, String context) {
        return bucketPolicyService.determineBucket(fileType, isPublic, context);
    }

    public void validateOwnerId(UUID ownerId) {
        fileValidationService.validateOwnerId(ownerId);
    }

    public boolean shouldBePublic(String context, String fileType) {
        return fileValidationService.shouldBePublic(context, fileType);
    }

    public boolean shouldBeEphemeral(String context, String fileType) {
        return fileValidationService.shouldBeEphemeral(context, fileType);
    }

    public String getDefaultPrivateBucket() {
        return bucketPolicyService.getDefaultPrivateBucket();
    }

    public String getDefaultPublicBucket() {
        return bucketPolicyService.getDefaultPublicBucket();
    }
}
