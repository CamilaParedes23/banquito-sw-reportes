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
@Table(name = "\"RESULTADO_PROCESAMIENTO_LINEA\"")
public class LineProcessingResult {

    @Id
    @Column(name = "\"ID_RESULTADO_PROCESAMIENTO_LINEA\"", nullable = false)
    private UUID lineProcessingResultId;

    @Column(name = "\"ID_EVENTO_ORIGEN\"", nullable = false)
    private UUID sourceEventId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"ID_LINEA\"", nullable = false, unique = true)
    private UUID lineId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"ESTADO_FINAL\"", nullable = false)
    private String finalStatus;

    @Column(name = "\"COBRABLE\"", nullable = false)
    private Boolean billable;

    @Column(name = "\"MONTO\"", nullable = false)
    private BigDecimal amount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"FUENTE_RESULTADO\"", nullable = false)
    private String resultSource;

    @Column(name = "\"ID_TRANSACCION_CORE\"")
    private String coreTransactionId;

    @Column(name = "\"ID_ARCHIVO_COMPENSACION\"")
    private UUID clearingFileId;

    @Column(name = "\"NOMBRE_ARCHIVO_COMPENSACION\"")
    private String clearingFileName;

    @Column(name = "\"CODIGO_RECHAZO\"")
    private String rejectionCode;

    @Column(name = "\"MOTIVO_RECHAZO\"")
    private String rejectionReason;

    @Column(name = "\"EMAIL_NOTIFICACION\"")
    private String notificationEmail;

    @Column(name = "\"FECHA_RECEPCION\"", nullable = false)
    private OffsetDateTime receivedAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public LineProcessingResult() {
    }

    public LineProcessingResult(UUID lineProcessingResultId) {
        this.lineProcessingResultId = lineProcessingResultId;
    }

    public UUID getLineProcessingResultId() { return lineProcessingResultId; }
    public void setLineProcessingResultId(UUID lineProcessingResultId) { this.lineProcessingResultId = lineProcessingResultId; }
    public UUID getSourceEventId() { return sourceEventId; }
    public void setSourceEventId(UUID sourceEventId) { this.sourceEventId = sourceEventId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getLineId() { return lineId; }
    public void setLineId(UUID lineId) { this.lineId = lineId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public String getFinalStatus() { return finalStatus; }
    public void setFinalStatus(String finalStatus) { this.finalStatus = finalStatus; }
    public Boolean getBillable() { return billable; }
    public void setBillable(Boolean billable) { this.billable = billable; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getResultSource() { return resultSource; }
    public void setResultSource(String resultSource) { this.resultSource = resultSource; }
    public String getCoreTransactionId() { return coreTransactionId; }
    public void setCoreTransactionId(String coreTransactionId) { this.coreTransactionId = coreTransactionId; }
    public UUID getClearingFileId() { return clearingFileId; }
    public void setClearingFileId(UUID clearingFileId) { this.clearingFileId = clearingFileId; }
    public String getClearingFileName() { return clearingFileName; }
    public void setClearingFileName(String clearingFileName) { this.clearingFileName = clearingFileName; }
    public String getRejectionCode() { return rejectionCode; }
    public void setRejectionCode(String rejectionCode) { this.rejectionCode = rejectionCode; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getNotificationEmail() { return notificationEmail; }
    public void setNotificationEmail(String notificationEmail) { this.notificationEmail = notificationEmail; }
    public OffsetDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(OffsetDateTime receivedAt) { this.receivedAt = receivedAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof LineProcessingResult that)) { return false; }
        return Objects.equals(lineProcessingResultId, that.lineProcessingResultId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineProcessingResultId);
    }

    @Override
    public String toString() {
        return "LineProcessingResult{lineProcessingResultId=" + lineProcessingResultId
                + ", batchId=" + batchId + ", lineId=" + lineId + ", finalStatus=" + finalStatus + "}";
    }
}
