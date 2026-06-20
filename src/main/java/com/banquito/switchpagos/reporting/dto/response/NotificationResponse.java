package com.banquito.switchpagos.reporting.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public class NotificationResponse {

    private UUID notificationUuid;
    private String status;
    private String message;
    private String channelType;
    private String recipient;
    private OffsetDateTime createdAt;

    public UUID getNotificationUuid() { return notificationUuid; }
    public void setNotificationUuid(UUID notificationUuid) { this.notificationUuid = notificationUuid; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getChannelType() { return channelType; }
    public void setChannelType(String channelType) { this.channelType = channelType; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
