package it.getinsight.message;

import it.getinsight.core.message.IMessageProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@RequiredArgsConstructor
public enum MessageProperty implements IMessageProperty {

    //see the file src/main/resources/api/messages_en_US.properties

    EXAMPLE_ERROR_MESSAGE("example.of.error"),
    REQUEST_NOT_FOUND_ERROR("request.not.found"),
    EXAMPLE_MESSAGE_WITH_PARAMETER("example.of.error.with.parameter"),
    APPROVE_NOT_AUTHORIZED("requester.approve.not.authorized"),
    USER_NOT_AUTHORIZED("user.not.authorized"),
    CLIENT_NOT_FOUND_ERROR("client.not.found"),
    APPROVERS_NOT_FOUND_ERROR("approvers.not.found"),
    ROLE_NOT_FOUND_PARENT_ERROR("role.not.found.parent"),
    ROLE_ALREADY_EXISTS_ERROR("role.already.exists"),
    ROLE_NOT_FOUND_ERROR("role.not.found"),
    CLIENT_NOT_PUBLISHED_ERROR("client.not.published"),
    USER_NOT_FOUND_ERROR("user.not.found"),;

    private final String key;

    private String[] args = {};

    @Override
    public String key() {
        return key;
    }

    @Override
    public IMessageProperty bind(String... pArgs) {
        this.args = ArrayUtils.isNotEmpty(pArgs) ? pArgs : null;
        return this;
    }

}
