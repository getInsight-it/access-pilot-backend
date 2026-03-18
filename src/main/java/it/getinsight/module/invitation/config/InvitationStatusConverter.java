package it.getinsight.module.invitation.config;

import it.getinsight.module.invitation.enuns.InvitationStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class InvitationStatusConverter implements Converter<String, InvitationStatus> {

    @Override
    public InvitationStatus convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return InvitationStatus.valueOf(source.trim().toUpperCase(Locale.ROOT));
    }
}
