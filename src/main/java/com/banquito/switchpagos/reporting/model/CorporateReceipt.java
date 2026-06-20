package com.banquito.switchpagos.reporting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"COMPROBANTE_CORPORATIVO\"")
public class CorporateReceipt {

    @Id
    @Column(name = "\"ID_COMPROBANTE_CORPORATIVO\"", nullable = false)
    private UUID corporateReceiptId;

    @Column(name = "\"ID_EVENTO_ORIGEN\"", nullable = false)
    private UUID sourceEventId;

    @Column(name = "\"ID_LOTE\"", nullable = false, unique = true)
    private UUID batchId;

    @Column(name = "\"ID_COBRO_COMISION\"")
    private UUID billingId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"RUC_EMPRESA\"")
    private String companyRuc;

    @Column(name = "\"NUMERO_CUENTA_MATRIZ\"")
    private String sourceAccountNumber;

    @Column(name = "\"TOTAL_LINEAS_RECIBIDAS\"", nullable = false)
    private Integer totalReceivedLines;

    @Column(name = "\"LINEAS_ON_US_EXITOSAS\"", nullable = false)
    private Integer successfulOnUsLines;

    @Column(name = "\"LINEAS_OFF_US_INCLUIDAS\"", nullable = false)
    private Integer includedOffUsLines;

    @Column(name = "\"LINEAS_RECHAZADAS\"", nullable = false)
    private Integer rejectedLines;

    @Column(name = "\"LINEAS_FALLIDAS\"", nullable = false)
    private Integer failedLines;

    @Column(name = "\"LINEAS_COBRABLES\"", nullable = false)
    private Integer billableLines;

    @Column(name = "\"MONTO_CONTROL_TOTAL\"")
    private BigDecimal totalControlAmount;

    @Column(name = "\"MONTO_PROCESADO_TOTAL\"", nullable = false)
    private BigDecimal totalProcessedAmount;

    @Column(name = "\"MONTO_REMANENTE\"")
    private BigDecimal remainingAmount;

    @Column(name = "\"ESTADO_AJUSTE_FONDEO\"")
    private String fundingAdjustmentStatus;

    @Column(name = "\"MONTO_LIBERADO\"")
    private BigDecimal releasedAmount;

    @Column(name = "\"TARIFA_UNITARIA\"")
    private BigDecimal unitFee;

    @Column(name = "\"SUBTOTAL_COMISION\"")
    private BigDecimal commissionSubtotal;

    @Column(name = "\"MONTO_IMPUESTO\"")
    private BigDecimal taxAmount;

    @Column(name = "\"MONTO_TOTAL_COBRADO\"")
    private BigDecimal totalChargedAmount;

    @Column(name = "\"MONEDA\"", nullable = false)
    private String currency;

    @Column(name = "\"ESTADO_COBRO\"")
    private String billingStatus;

    @Column(name = "\"ID_COBRO_COMISION_CORE\"")
    private String coreCommissionChargeId;

    @Column(name = "\"ID_TRANSACCION_CORE_LIBERACION_FONDEO\"")
    private String fundingReleaseCoreTransactionId;

    @Column(name = "\"ID_REPORTE_NOVEDADES\"")
    private UUID noveltyReportId;

    @Column(name = "\"RUTA_ARCHIVO_REPORTE_NOVEDADES\"")
    private String noveltyReportFilePath;

    @Transient
    private String coreFundingId;

    @Transient
    private BigDecimal fundedAmount;

    @Transient
    private String clearingFileName;

    @Transient
    private String clearingFilePath;

    @Column(name = "\"NOMBRE_ARCHIVO\"", nullable = false)
    private String fileName;

    @Column(name = "\"RUTA_ARCHIVO\"", nullable = false)
    private String filePath;

    @Column(name = "\"FECHA_GENERACION\"", nullable = false)
    private OffsetDateTime generatedAt;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public CorporateReceipt() {
    }

    public CorporateReceipt(UUID corporateReceiptId) {
        this.corporateReceiptId = corporateReceiptId;
    }

    public UUID getCorporateReceiptId() { return corporateReceiptId; }
    public void setCorporateReceiptId(UUID corporateReceiptId) { this.corporateReceiptId = corporateReceiptId; }
    public UUID getSourceEventId() { return sourceEventId; }
    public void setSourceEventId(UUID sourceEventId) { this.sourceEventId = sourceEventId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getBillingId() { return billingId; }
    public void setBillingId(UUID billingId) { this.billingId = billingId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public String getCompanyRuc() { return companyRuc; }
    public void setCompanyRuc(String companyRuc) { this.companyRuc = companyRuc; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public Integer getTotalReceivedLines() { return totalReceivedLines; }
    public void setTotalReceivedLines(Integer totalReceivedLines) { this.totalReceivedLines = totalReceivedLines; }
    public Integer getSuccessfulOnUsLines() { return successfulOnUsLines; }
    public void setSuccessfulOnUsLines(Integer successfulOnUsLines) { this.successfulOnUsLines = successfulOnUsLines; }
    public Integer getIncludedOffUsLines() { return includedOffUsLines; }
    public void setIncludedOffUsLines(Integer includedOffUsLines) { this.includedOffUsLines = includedOffUsLines; }
    public Integer getRejectedLines() { return rejectedLines; }
    public void setRejectedLines(Integer rejectedLines) { this.rejectedLines = rejectedLines; }
    public Integer getFailedLines() { return failedLines; }
    public void setFailedLines(Integer failedLines) { this.failedLines = failedLines; }
    public Integer getBillableLines() { return billableLines; }
    public void setBillableLines(Integer billableLines) { this.billableLines = billableLines; }
    public BigDecimal getTotalControlAmount() { return totalControlAmount; }
    public void setTotalControlAmount(BigDecimal totalControlAmount) { this.totalControlAmount = totalControlAmount; }
    public BigDecimal getTotalProcessedAmount() { return totalProcessedAmount; }
    public void setTotalProcessedAmount(BigDecimal totalProcessedAmount) { this.totalProcessedAmount = totalProcessedAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public String getFundingAdjustmentStatus() { return fundingAdjustmentStatus; }
    public void setFundingAdjustmentStatus(String fundingAdjustmentStatus) { this.fundingAdjustmentStatus = fundingAdjustmentStatus; }
    public BigDecimal getReleasedAmount() { return releasedAmount; }
    public void setReleasedAmount(BigDecimal releasedAmount) { this.releasedAmount = releasedAmount; }
    public BigDecimal getUnitFee() { return unitFee; }
    public void setUnitFee(BigDecimal unitFee) { this.unitFee = unitFee; }
    public BigDecimal getCommissionSubtotal() { return commissionSubtotal; }
    public void setCommissionSubtotal(BigDecimal commissionSubtotal) { this.commissionSubtotal = commissionSubtotal; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalChargedAmount() { return totalChargedAmount; }
    public void setTotalChargedAmount(BigDecimal totalChargedAmount) { this.totalChargedAmount = totalChargedAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getBillingStatus() { return billingStatus; }
    public void setBillingStatus(String billingStatus) { this.billingStatus = billingStatus; }
    public String getCoreCommissionChargeId() { return coreCommissionChargeId; }
    public void setCoreCommissionChargeId(String coreCommissionChargeId) { this.coreCommissionChargeId = coreCommissionChargeId; }
    public String getFundingReleaseCoreTransactionId() { return fundingReleaseCoreTransactionId; }
    public void setFundingReleaseCoreTransactionId(String fundingReleaseCoreTransactionId) { this.fundingReleaseCoreTransactionId = fundingReleaseCoreTransactionId; }
    public UUID getNoveltyReportId() { return noveltyReportId; }
    public void setNoveltyReportId(UUID noveltyReportId) { this.noveltyReportId = noveltyReportId; }
    public String getNoveltyReportFilePath() { return noveltyReportFilePath; }
    public void setNoveltyReportFilePath(String noveltyReportFilePath) { this.noveltyReportFilePath = noveltyReportFilePath; }
    public String getCoreFundingId() { return coreFundingId; }
    public void setCoreFundingId(String coreFundingId) { this.coreFundingId = coreFundingId; }
    public BigDecimal getFundedAmount() { return fundedAmount; }
    public void setFundedAmount(BigDecimal fundedAmount) { this.fundedAmount = fundedAmount; }
    public String getClearingFileName() { return clearingFileName; }
    public void setClearingFileName(String clearingFileName) { this.clearingFileName = clearingFileName; }
    public String getClearingFilePath() { return clearingFilePath; }
    public void setClearingFilePath(String clearingFilePath) { this.clearingFilePath = clearingFilePath; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof CorporateReceipt that)) { return false; }
        return Objects.equals(corporateReceiptId, that.corporateReceiptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corporateReceiptId);
    }

    @Override
    public String toString() {
        return "CorporateReceipt{corporateReceiptId=" + corporateReceiptId + ", batchId=" + batchId + "}";
    }
}
