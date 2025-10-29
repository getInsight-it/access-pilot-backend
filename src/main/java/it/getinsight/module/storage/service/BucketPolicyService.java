package it.getinsight.module.storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class BucketPolicyService {

    private static final String PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "private-getinsight-accesspilot-docs";
    private static final String PUBLIC_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "public-getinsight-accesspilot-docs";


    public String determineBucket(String fileType, boolean isPublic, String context) {
        if (isPublic) {
            log.debug("Using public bucket for file type: {}", fileType);
            return PUBLIC_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
        }

        log.debug("Using private bucket for file type: {} in context: {}", fileType, context);
        return PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
    }


    public String getDefaultPrivateBucket() {
        return PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
    }


    public String getDefaultPublicBucket() {
        return PUBLIC_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
    }
}
