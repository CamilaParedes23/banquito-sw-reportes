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
@Table(name = "\"LINEA_REPORTE_NOVEDADES\"")
public class NoveltyReportLine {

    @Id
    @Column(name = "\"ID_LINEA_REPORTE_NOVEDADES\"", nullable = false)
    private UUID noveltyReportLineId;

    @Column(name = "\"ID_REPORTE_NOVEDADES\"", nullable = false)
    private UUID noveltyReportId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"ID_LINEA\"", nullable = false, unique = true)
    private UUID lineId;

    @Column(name = "\"NUMERO_SECUENCIA\"")
    private Integer sequenceNumber;

    @Column(name = "\"TIPO_RUTA\"")
    private String routeType;

    @Column(name = "\"IDENTIFICACION_BENEFICIARIO\"")
    private String beneficiaryIdentification;

    @Column(name = "\"NOMBRE_BENEFICIARIO\"")
    private String beneficiaryName;

    @Column(name = "\"NUMERO_CUENTA_DESTINO\"")
    private String destinationAccountNumber;

    @Column(name = "\"CODIGO_ENRUTAMIENTO\"")
    private String routingCode;

    @Column(name = "\"MONTO\"", nullable = false)
    private BigDecimal amount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"ESTADO_FINAL\"", nullable = false)
    private String finalStatus;

    @Column(name = "\"COBRABLE\"", nullable = false)
    private Boolean billable;

    @Column(name = "\"CODIGO_RECHAZO\"")
    private String rejectionCode;

    @Column(name = "\"MOTIVO_RECHAZO\"")
    private String rejectionReason;

    @Column(name = "\"MENSAJE\"")
    private String message;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    public NoveltyReportLine() {
    }

    public NoveltyReportLine(UUID noveltyReportLineId) {
        this.noveltyReportLineId = noveltyReportLineId;
    }

    public UUID getNoveltyReportLineId() { return noveltyReportLineId; }
    public void setNoveltyReportLineId(UUID noveltyReportLineId) { this.noveltyReportLineId = noveltyReportLineId; }
    public UUID getNoveltyReportId() { return noveltyReportId; }
    public void setNoveltyReportId(UUID noveltyReportId) { this.noveltyReportId = noveltyReportId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getLineId() { return lineId; }
    public void setLineId(UUID lineId) { this.lineId = lineId; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public String getRouteType() { return routeType; }
    public void setRouteType(String routeType) { this.routeType = routeType; }
    public String getBeneficiaryIdentification() { return beneficiaryIdentification; }
    public void setBeneficiaryIdentification(String beneficiaryIdentification) { this.beneficiaryIdentification = beneficiaryIdentification; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
    public String getRoutingCode() { return routingCode; }
    public void setRoutingCode(String routingCode) { this.routingCode = routingCode; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getFinalStatus() { return finalStatus; }
    public void setFinalStatus(String finalStatus) { this.finalStatus = finalStatus; }
    public Boolean getBillable() { return billable; }
    public void setBillable(Boolean billable) { this.billable = billable; }
    public String getRejectionCode() { return rejectionCode; }
    public void setRejectionCode(String rejectionCode) { this.rejectionCode = rejectionCode; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof NoveltyReportLine that)) { return false; }
        return Objects.equals(noveltyReportLineId, that.noveltyReportLineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noveltyReportLineId);
    }

    @Override
    public String toString() {
        return "NoveltyReportLine{noveltyReportLineId=" + noveltyReportLineId + ", lineId=" + lineId + "}";
    }
}
