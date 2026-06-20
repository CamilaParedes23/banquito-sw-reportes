package com.banquito.switchpagos.reporting.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationOrdersResponse {

    private UUID batchId;
    private Integer totalOrders;
    private List<NotificationOrderResponse> notifications = new ArrayList<>();

    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
    public List<NotificationOrderResponse> getNotifications() { return notifications; }
    public void setNotifications(List<NotificationOrderResponse> notifications) { this.notifications = notifications; }
}
