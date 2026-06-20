package com.banquito.switchpagos.reporting.dto.request;

import java.util.Map;

public class NotificationRequest {

    private String correlationId;
    private String eventType;
    private String originService;
    private String priority;
    private String channelType;
    private String recipient;
    private String recipientName;
    private String templateCode;
    private String subject;
    private String body;
    private Map<String, String> payload;
    private String evidenceDocumentUuid;

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getOriginService() { return originService; }
    public void setOriginService(String originService) { this.originService = originService; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getChannelType() { return channelType; }
    public void setChannelType(String channelType) { this.channelType = channelType; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public Map<String, String> getPayload() { return payload; }
    public void setPayload(Map<String, String> payload) { this.payload = payload; }
    public String getEvidenceDocumentUuid() { return evidenceDocumentUuid; }
    public void setEvidenceDocumentUuid(String evidenceDocumentUuid) { this.evidenceDocumentUuid = evidenceDocumentUuid; }
}
