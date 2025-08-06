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
    REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE("request.error.when.trying.to.assign.role"),
    EXAMPLE_MESSAGE_WITH_PARAMETER("example.of.error.with.parameter"),
    APPROVE_NOT_AUTHORIZED("requester.approve.not.authorized"),
    USER_NOT_AUTHORIZED("user.not.authorized"),
    CLIENT_NOT_FOUND_ERROR("client.not.found"),
    CLIENT_ALREADY_EXISTS_ERROR("client.already.exists"),
    APPROVERS_NOT_FOUND_ERROR("approvers.not.found"),
    ROLE_NOT_FOUND_PARENT_ERROR("role.not.found.parent"),
    ROLE_ALREADY_EXISTS_ERROR("role.already.exists"),
    ROLE_ALREADY_EXISTS_IDP_ERROR("role.already.exists.at.idp"),
    ROLE_NOT_FOUND_ERROR("role.not.found"),
    ERROR_VALIDATION_PATTERN_ROLE_NAME("error.validation.pattern.role.name"),
    CLIENT_NOT_PUBLISHED_ERROR("client.not.published"),
    USER_NOT_FOUND_ERROR("user.not.found"),
    REQUIRED_FIELD_WITH_PARAMETER("required.field.with.parameter"),
    NOTIFICATION_NOT_FOUND_ERROR("notification.not.found"),
    ERROR_IMPORT_CSV("error.import"),
    ERROR_EXPORT_CSV("error.export"),
    LEVEL_NOT_FOUND_ERROR("level.not.found"),
    LEVEL_NOT_ATTACHED_ERROR("level.not.attached"),
    UPDATE_BUILT_IN_LEVEL("error.update.built.in.level"),
    UPDATE_BUILT_IN_ITEM("error.update.built.in.level.parent"),
    CREATE_BUILT_IN_ITEM("error.create.built.in.level.parent"),
    CREATE_BUILT_IN_LEVEL("error.create.built.in.level"),
    ROLE_INVALID_CODE_ERROR("role.invalid.code"),
    ITEM_NOT_FOUND_ERROR("item.not.found"),
    ITEM_ALREADY_EXISTS_ERROR("item.already.exists"),
    CODE_ITEM_NOT_FOUND_FOR_ROLE("code.item.not.found.for.role"),
    LEVEL_ALREADY_EXISTS_ERROR("level.already.exists"),
    ERROR_WRITING_JSON("error.writing.json"),
    ERROR_READING_JSON("error.reading.json"),
    ERROR_CONFIGURATION_NOT_FOUND("error.configuration.not.found"),
    ERROR_CONFIGURATION_UPDATE("error.configuration.update"),
    ERROR_CONFIGURATION_CREATE("error.configuration.create"),
    ERROR_CONFIGURATION_DELETE("error.configuration.delete"),
    NOTIFICATION_NOT_FOUND("notification.not.found"),
    ATTACHMENTS_QUANTITY_ERROR("attachments.quantity.error"),
    ATTACHMENTS_EXTENSION_NOT_ALLOWED_ERROR("attachments.extension.not.allowed.error"),
    ATTACHMENTS_REQUIRED_ERROR("attachments.required.error"),
    ATTACHMENTS_NAME_DUPLICATE_ERROR("attachments.name.duplicate.error"),
    ATTACHMENTS_MULTIPLE_ACTIVE_ERROR("attachments.multiple.active.error"),
    ERROR_UPDATE_LEVEL_TYPE("error.update.level.type"),
    ERROR_UPDATE_LEVEL_PARENT_WITH_ITEMS("error.update.level.parent.with.items"),
    ROLE_WITH_PENDING_REQUESTS_ERROR("role.with.pending.requests"),

    ;

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
