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
@Table(name = "\"RESUMEN_PROCESAMIENTO_LOTE\"")
public class BatchProcessingSummary {

    @Id
    @Column(name = "\"ID_RESUMEN_PROCESAMIENTO_LOTE\"", nullable = false)
    private UUID batchProcessingSummaryId;

    @Column(name = "\"ID_LOTE\"", nullable = false, unique = true)
    private UUID batchId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"RUC_EMPRESA\"")
    private String companyRuc;

    @Column(name = "\"NUMERO_CUENTA_MATRIZ\"")
    private String sourceAccountNumber;

    @Column(name = "\"ID_FONDEO_CORE\"")
    private String coreFundingId;

    @Column(name = "\"TOTAL_LINEAS_ESPERADAS\"")
    private Integer expectedTotalLines;

    @Column(name = "\"LINEAS_OBSERVADAS\"", nullable = false)
    private Integer observedLines;

    @Column(name = "\"LINEAS_RESULTADO_FINAL\"", nullable = false)
    private Integer finalResultLines;

    @Column(name = "\"LINEAS_ON_US_ACREDITADAS\"", nullable = false)
    private Integer onUsCreditedLines;

    @Column(name = "\"LINEAS_OFF_US_INCLUIDAS\"", nullable = false)
    private Integer offUsIncludedLines;

    @Column(name = "\"LINEAS_RECHAZADAS\"", nullable = false)
    private Integer rejectedLines;

    @Column(name = "\"LINEAS_FALLIDAS\"", nullable = false)
    private Integer failedLines;

    @Column(name = "\"LINEAS_COBRABLES\"", nullable = false)
    private Integer billableLines;

    @Column(name = "\"MONTO_CONTROL\"")
    private BigDecimal controlAmount;

    @Column(name = "\"MONTO_PROCESADO\"", nullable = false)
    private BigDecimal processedAmount;

    @Column(name = "\"MONTO_REMANENTE\"")
    private BigDecimal remainingAmount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"ESTADO\"", nullable = false)
    private String status;

    @Column(name = "\"EVENTO_COMPLETADO_PUBLICADO\"", nullable = false)
    private Boolean completedEventPublished;

    @Column(name = "\"ID_EVENTO_COMPLETADO\"")
    private UUID completedEventId;

    @Column(name = "\"FECHA_PRIMERA_OBSERVACION\"", nullable = false)
    private OffsetDateTime firstObservedAt;

    @Column(name = "\"FECHA_FINALIZACION\"")
    private OffsetDateTime completedAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public BatchProcessingSummary() {
    }

    public BatchProcessingSummary(UUID batchProcessingSummaryId) {
        this.batchProcessingSummaryId = batchProcessingSummaryId;
    }

    public UUID getBatchProcessingSummaryId() { return batchProcessingSummaryId; }
    public void setBatchProcessingSummaryId(UUID batchProcessingSummaryId) { this.batchProcessingSummaryId = batchProcessingSummaryId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public String getCompanyRuc() { return companyRuc; }
    public void setCompanyRuc(String companyRuc) { this.companyRuc = companyRuc; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getCoreFundingId() { return coreFundingId; }
    public void setCoreFundingId(String coreFundingId) { this.coreFundingId = coreFundingId; }
    public Integer getExpectedTotalLines() { return expectedTotalLines; }
    public void setExpectedTotalLines(Integer expectedTotalLines) { this.expectedTotalLines = expectedTotalLines; }
    public Integer getObservedLines() { return observedLines; }
    public void setObservedLines(Integer observedLines) { this.observedLines = observedLines; }
    public Integer getFinalResultLines() { return finalResultLines; }
    public void setFinalResultLines(Integer finalResultLines) { this.finalResultLines = finalResultLines; }
    public Integer getOnUsCreditedLines() { return onUsCreditedLines; }
    public void setOnUsCreditedLines(Integer onUsCreditedLines) { this.onUsCreditedLines = onUsCreditedLines; }
    public Integer getOffUsIncludedLines() { return offUsIncludedLines; }
    public void setOffUsIncludedLines(Integer offUsIncludedLines) { this.offUsIncludedLines = offUsIncludedLines; }
    public Integer getRejectedLines() { return rejectedLines; }
    public void setRejectedLines(Integer rejectedLines) { this.rejectedLines = rejectedLines; }
    public Integer getFailedLines() { return failedLines; }
    public void setFailedLines(Integer failedLines) { this.failedLines = failedLines; }
    public Integer getBillableLines() { return billableLines; }
    public void setBillableLines(Integer billableLines) { this.billableLines = billableLines; }
    public BigDecimal getControlAmount() { return controlAmount; }
    public void setControlAmount(BigDecimal controlAmount) { this.controlAmount = controlAmount; }
    public BigDecimal getProcessedAmount() { return processedAmount; }
    public void setProcessedAmount(BigDecimal processedAmount) { this.processedAmount = processedAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getCompletedEventPublished() { return completedEventPublished; }
    public void setCompletedEventPublished(Boolean completedEventPublished) { this.completedEventPublished = completedEventPublished; }
    public UUID getCompletedEventId() { return completedEventId; }
    public void setCompletedEventId(UUID completedEventId) { this.completedEventId = completedEventId; }
    public OffsetDateTime getFirstObservedAt() { return firstObservedAt; }
    public void setFirstObservedAt(OffsetDateTime firstObservedAt) { this.firstObservedAt = firstObservedAt; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof BatchProcessingSummary that)) { return false; }
        return Objects.equals(batchProcessingSummaryId, that.batchProcessingSummaryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchProcessingSummaryId);
    }

    @Override
    public String toString() {
        return "BatchProcessingSummary{batchProcessingSummaryId=" + batchProcessingSummaryId
                + ", batchId=" + batchId + ", status=" + status + "}";
    }
}
