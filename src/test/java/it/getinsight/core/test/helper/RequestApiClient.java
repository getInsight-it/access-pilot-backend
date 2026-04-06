package it.getinsight.core.test.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.enuns.RequestAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestApiClient {

    private final TestRestTemplate testRestTemplate;
    private final ObjectMapper objectMapper;
    private final TokenHelper tokenHelper;

    private static final String BASE_PATH = "/v1/requests";

    public ResponseEntity<Void> createRequest(RequestCreateDTO dto, String username, String password,
                                              MultiValueMap<String, MultipartFile> attachments) {
        log.debug("Creating request for user: {}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            body.add("request", objectMapper.writeValueAsString(dto));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request DTO", e);
        }

        if (attachments != null && !attachments.isEmpty()) {
            attachments.forEach((key, files) -> {
                files.forEach(file -> body.add(key, file.getResource()));
            });
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Void> response = testRestTemplate.exchange(
            BASE_PATH,
            HttpMethod.POST,
            request,
            Void.class
        );
        throwIfError(response);

        log.debug("Request created with status: {}, Location: {}",
            response.getStatusCode(), response.getHeaders().getLocation());

        return response;
    }

    public ResponseEntity<Void> createRequest(RequestCreateDTO dto, String username, String password) {
        return createRequest(dto, username, password, null);
    }

    public ResponseEntity<Void> updateRequestStatus(Long id, RequestUpdateDTO dto, String username, String password) {
        log.debug("Updating request {} by user: {}", id, username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("request", objectMapper.writeValueAsString(dto));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize update DTO", e);
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Void> response = testRestTemplate.exchange(
            BASE_PATH + "/{id}",
            HttpMethod.PUT,
            request,
            Void.class,
            id
        );
        throwIfError(response);

        log.debug("Request {} updated with status: {}", id, response.getStatusCode());

        return response;
    }

    public RequestDTO getRequest(Long id, String username, String password) {
        log.debug("Getting request {} for user: {}", id, username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<RequestDTO> response = testRestTemplate.exchange(
            BASE_PATH + "/{id}",
            HttpMethod.GET,
            request,
            RequestDTO.class,
            id
        );

        return response.getBody();
    }

    public PageableResponseModel<RequestDTO> listMyRequests(String username, String password,
                                                             int pageIndex, int pageSize) {
        log.debug("Listing requests for user: {} (page {} size {})", username, pageIndex, pageSize);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String url = String.format("%s/me/paginated?type=created&pageIndex=%d&pageSize=%d",
            BASE_PATH, pageIndex, pageSize);

        ResponseEntity<PageableResponseModel<RequestDTO>> response = testRestTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
                new ParameterizedTypeReference<>() {
            }
        );

        return normalizePageResponse(response.getBody());
    }

    public PageableResponseModel<RequestDTO> listAssignedRequests(String username, String password,
                                                                   RequestFilterDTO filter,
                                                                   int pageIndex, int pageSize) {
        log.debug("Listing assigned requests for user: {}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        StringBuilder url = new StringBuilder(String.format(
            "%s/me/paginated?type=assigned&pageIndex=%d&pageSize=%d",
            BASE_PATH, pageIndex, pageSize));

        if (filter != null) {
            if (filter.status() != null) {
                url.append("&status=").append(filter.status());
            }
            if (filter.description() != null) {
                url.append("&description=").append(filter.description());
            }
            if (filter.protocolCode() != null) {
                url.append("&protocolCode=").append(filter.protocolCode());
            }
        }

        ResponseEntity<PageableResponseModel<RequestDTO>> response = testRestTemplate.exchange(
            url.toString(),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {
            }
        );

        return normalizePageResponse(response.getBody());
    }

    public List<RequestAction> getMyAvailableActions(Long id, String username, String password) {
        log.debug("Getting available actions for request {} and user: {}", id, username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<RequestAction>> response = testRestTemplate.exchange(
            BASE_PATH + "/{id}/my-available-actions",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {
            },
            id
        );

        return response.getBody();
    }

    public PageableResponseModel<RequestDTO> listAllRequests(String username, String password,
                                                              RequestFilterDTO filter,
                                                              int pageIndex, int pageSize) {
        log.debug("Listing all requests for user: {}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenHelper.getToken(username, password));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        StringBuilder url = new StringBuilder(String.format(
            "%s/paginated?pageIndex=%d&pageSize=%d",
            BASE_PATH, pageIndex, pageSize));

        if (filter != null) {
            if (filter.status() != null) {
                url.append("&status=").append(filter.status());
            }
        }

        ResponseEntity<PageableResponseModel<RequestDTO>> response = testRestTemplate.exchange(
            url.toString(),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {
            }
        );

        return normalizePageResponse(response.getBody());
    }

    public Long extractIdFromLocation(ResponseEntity<?> response) {
        if (response.getHeaders().getLocation() == null) {
            throw new IllegalArgumentException("Location header not present");
        }
        String location = response.getHeaders().getLocation().toString();
        String[] parts = location.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    private PageableResponseModel<RequestDTO> normalizePageResponse(PageableResponseModel<RequestDTO> body) {
        if (body == null) {
            return PageableResponseModel.of(0L, List.of());
        }
        if (body.getItems() == null) {
            body.setItems(List.of());
        }
        if (body.getTotal() == null) {
            body.setTotal(0L);
        }
        return body;
    }

    private void throwIfError(ResponseEntity<?> response) {
        HttpStatusCode status = response.getStatusCode();
        if (status.is4xxClientError()) {
            throw HttpClientErrorException.create(status, status.toString(), response.getHeaders(), null, null);
        }
        if (status.is5xxServerError()) {
            throw HttpServerErrorException.create(status, status.toString(), response.getHeaders(), null, null);
        }
    }
}
