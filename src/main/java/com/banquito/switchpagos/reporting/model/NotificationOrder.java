package com.banquito.switchpagos.reporting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"ORDEN_NOTIFICACION\"")
public class NotificationOrder {

    @Id
    @Column(name = "\"ID_ORDEN_NOTIFICACION\"", nullable = false)
    private UUID notificationOrderId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"ID_LINEA\"", nullable = false, unique = true)
    private UUID lineId;

    @Column(name = "\"EMAIL_NOTIFICACION\"")
    private String notificationEmail;

    @Column(name = "\"NOMBRE_BENEFICIARIO\"")
    private String beneficiaryName;

    @Column(name = "\"MONTO\"", nullable = false)
    private BigDecimal amount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"ESTADO_FINAL\"", nullable = false)
    private String finalStatus;

    @Column(name = "\"MENSAJE\"")
    private String message;

    @Column(name = "\"ESTADO\"", nullable = false)
    private String status;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    public NotificationOrder() {
    }

    public NotificationOrder(UUID notificationOrderId) {
        this.notificationOrderId = notificationOrderId;
    }

    public UUID getNotificationOrderId() { return notificationOrderId; }
    public void setNotificationOrderId(UUID notificationOrderId) { this.notificationOrderId = notificationOrderId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getLineId() { return lineId; }
    public void setLineId(UUID lineId) { this.lineId = lineId; }
    public String getNotificationEmail() { return notificationEmail; }
    public void setNotificationEmail(String notificationEmail) { this.notificationEmail = notificationEmail; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getFinalStatus() { return finalStatus; }
    public void setFinalStatus(String finalStatus) { this.finalStatus = finalStatus; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof NotificationOrder that)) { return false; }
        return Objects.equals(notificationOrderId, that.notificationOrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationOrderId);
    }

    @Override
    public String toString() {
        return "NotificationOrder{notificationOrderId=" + notificationOrderId + ", lineId=" + lineId + "}";
    }
}
