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
@Table(name = "\"OBSERVACION_LINEA_LOTE\"")
public class BatchLineObservation {

    @Id
    @Column(name = "\"ID_OBSERVACION_LINEA_LOTE\"", nullable = false)
    private UUID batchLineObservationId;

    @Column(name = "\"ID_EVENTO_ORIGEN\"", nullable = false)
    private UUID sourceEventId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"ID_LINEA\"", nullable = false, unique = true)
    private UUID lineId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"NUMERO_SECUENCIA\"")
    private Integer sequenceNumber;

    @Column(name = "\"RUC_EMPRESA\"")
    private String companyRuc;

    @Column(name = "\"NUMERO_CUENTA_MATRIZ\"")
    private String sourceAccountNumber;

    @Column(name = "\"ID_FONDEO_CORE\"")
    private String coreFundingId;

    @Column(name = "\"MONTO\"", nullable = false)
    private BigDecimal amount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"TOTAL_LINEAS_LOTE\"")
    private Integer batchTotalLines;

    @Column(name = "\"MONTO_CONTROL_LOTE\"")
    private BigDecimal batchControlAmount;

    @Column(name = "\"IDENTIFICACION_BENEFICIARIO\"")
    private String beneficiaryIdentification;

    @Column(name = "\"NOMBRE_BENEFICIARIO\"")
    private String beneficiaryName;

    @Column(name = "\"NUMERO_CUENTA_DESTINO\"")
    private String destinationAccountNumber;

    @Column(name = "\"CODIGO_ENRUTAMIENTO\"")
    private String routingCode;

    @Column(name = "\"REFERENCIA\"")
    private String reference;

    @Column(name = "\"EMAIL_NOTIFICACION\"")
    private String notificationEmail;

    @Column(name = "\"FECHA_OBSERVACION\"", nullable = false)
    private OffsetDateTime observedAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public BatchLineObservation() {
    }

    public BatchLineObservation(UUID batchLineObservationId) {
        this.batchLineObservationId = batchLineObservationId;
    }

    public UUID getBatchLineObservationId() { return batchLineObservationId; }
    public void setBatchLineObservationId(UUID batchLineObservationId) { this.batchLineObservationId = batchLineObservationId; }
    public UUID getSourceEventId() { return sourceEventId; }
    public void setSourceEventId(UUID sourceEventId) { this.sourceEventId = sourceEventId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getLineId() { return lineId; }
    public void setLineId(UUID lineId) { this.lineId = lineId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public String getCompanyRuc() { return companyRuc; }
    public void setCompanyRuc(String companyRuc) { this.companyRuc = companyRuc; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getCoreFundingId() { return coreFundingId; }
    public void setCoreFundingId(String coreFundingId) { this.coreFundingId = coreFundingId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Integer getBatchTotalLines() { return batchTotalLines; }
    public void setBatchTotalLines(Integer batchTotalLines) { this.batchTotalLines = batchTotalLines; }
    public BigDecimal getBatchControlAmount() { return batchControlAmount; }
    public void setBatchControlAmount(BigDecimal batchControlAmount) { this.batchControlAmount = batchControlAmount; }
    public String getBeneficiaryIdentification() { return beneficiaryIdentification; }
    public void setBeneficiaryIdentification(String beneficiaryIdentification) { this.beneficiaryIdentification = beneficiaryIdentification; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
    public String getRoutingCode() { return routingCode; }
    public void setRoutingCode(String routingCode) { this.routingCode = routingCode; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNotificationEmail() { return notificationEmail; }
    public void setNotificationEmail(String notificationEmail) { this.notificationEmail = notificationEmail; }
    public OffsetDateTime getObservedAt() { return observedAt; }
    public void setObservedAt(OffsetDateTime observedAt) { this.observedAt = observedAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof BatchLineObservation that)) { return false; }
        return Objects.equals(batchLineObservationId, that.batchLineObservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchLineObservationId);
    }

    @Override
    public String toString() {
        return "BatchLineObservation{batchLineObservationId=" + batchLineObservationId
                + ", batchId=" + batchId + ", lineId=" + lineId + "}";
    }
}
