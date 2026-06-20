package com.banquito.switchpagos.reporting.client;

import java.util.UUID;

public class NotificationClientResult {

    private final Boolean successful;
    private final UUID notificationUuid;
    private final String status;
    private final String message;

    public NotificationClientResult(Boolean successful, UUID notificationUuid, String status, String message) {
        this.successful = successful;
        this.notificationUuid = notificationUuid;
        this.status = status;
        this.message = message;
    }

    public Boolean getSuccessful() { return successful; }
    public UUID getNotificationUuid() { return notificationUuid; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
