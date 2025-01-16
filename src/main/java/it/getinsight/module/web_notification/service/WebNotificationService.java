package it.getinsight.module.web_notification.service;


import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.dto.EmailFilterDTO;
import it.getinsight.module.notification.dto.Notification;
import it.getinsight.module.notification.service.NotificationSender;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;
import it.getinsight.module.web_notification.dto.WebNotificationFilterDTO;
import it.getinsight.module.web_notification.entity.WebNotificationEntity;
import it.getinsight.module.web_notification.mapper.WebNotificationMapper;
import it.getinsight.module.web_notification.repository.WebNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class WebNotificationService implements NotificationSender {

    private final WebNotificationRepository repository;
    private final WebNotificationMapper mapper;

    public PageableResponseModel<WebNotificationDTO> getNotificationsFull(PageableRequestModel<WebNotificationFilterDTO> configPage) {
        Optional<WebNotificationFilterDTO> filter = configPage
            .getFilter();
        final var model = filter
            .map(mapper::fromFilter)
            .map(mapper::toEntity)
            .orElse(new WebNotificationEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("user.id", ExampleMatcher.GenericPropertyMatcher::exact);

        final var example = Example.of(model, matcher);

        final var page = repository.findAll(example,PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(mapper.toDto(page.getContent()), page.getTotalElements());
    }


    @Override
    public Notification send(Notification notification) {
        if (!(notification instanceof WebNotificationDTO)) {
            return null;
        }
        WebNotificationDTO webNotificationDTO = (WebNotificationDTO) notification;
        log.info("Sending web notification: {}", webNotificationDTO);
        return mapper.toDto(repository.save(mapper.toEntity(webNotificationDTO)));
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
