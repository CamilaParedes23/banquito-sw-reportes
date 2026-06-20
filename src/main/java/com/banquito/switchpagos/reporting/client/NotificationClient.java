package com.banquito.switchpagos.reporting.client;

import com.banquito.switchpagos.reporting.dto.request.NotificationRequest;
import com.banquito.switchpagos.reporting.dto.response.NotificationResponse;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class NotificationClient {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationClient.class);

    private final RestClient restClient;
    private final String requestsPath;
    private final NotificationKongTokenProvider tokenProvider;

    public NotificationClient(
            @Value("${notification.kong.base-url}") String baseUrl,
            @Value("${notification.kong.requests-path}") String requestsPath,
            @Value("${notification.kong.connect-timeout-ms}") Long connectTimeoutMs,
            @Value("${notification.kong.read-timeout-ms}") Long readTimeoutMs,
            NotificationKongTokenProvider tokenProvider) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));
        this.restClient = RestClient.builder()
                .baseUrl(normalizeBaseUrl(baseUrl))
                .requestFactory(requestFactory)
                .build();
        this.requestsPath = normalizePath(requestsPath);
        this.tokenProvider = tokenProvider;
    }

    public NotificationClientResult requestNotification(NotificationRequest request) {
        try {
            RestClient.RequestBodySpec requestSpec = restClient.post()
                    .uri(requestsPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
            requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getBearerToken());
            NotificationResponse response = requestSpec
                    .body(request)
                    .retrieve()
                    .body(NotificationResponse.class);
            return new NotificationClientResult(
                    Boolean.TRUE,
                    response == null ? null : response.getNotificationUuid(),
                    response == null ? null : response.getStatus(),
                    "Notification Service acepto la solicitud");
        } catch (RestClientResponseException exception) {
            String message = "Notification Service rechazo solicitud. httpStatus="
                    + exception.getStatusCode().value();
            LOG.warn("{} responseBody={}", message, limit(exception.getResponseBodyAsString(), 300));
            return new NotificationClientResult(Boolean.FALSE, null, "HTTP_" + exception.getStatusCode().value(), message);
        } catch (ResourceAccessException exception) {
            String message = "No fue posible conectar con Notification Service";
            LOG.warn("{}: {}", message, exception.getMessage());
            return new NotificationClientResult(Boolean.FALSE, null, "CONNECTION_ERROR", message);
        } catch (RuntimeException exception) {
            String message = "Error tecnico controlado al solicitar Notification Service";
            LOG.warn("{}: {}", message, exception.getMessage());
            return new NotificationClientResult(Boolean.FALSE, null, "TECHNICAL_ERROR", message);
        }
    }

    private String normalizeBaseUrl(String baseUrl) {
        String value = baseUrl == null || baseUrl.isBlank() ? "http://localhost:8000" : baseUrl.trim();
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "/api/v1/notifications/requests";
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    private String limit(String value, Integer maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
