package com.banquito.switchpagos.reporting.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BatchCommissionResponse {

    private UUID batchId;
    private Integer billableLines;
    private BigDecimal unitFee;
    private BigDecimal commissionSubtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalCommission;
    private String billingStatus;
    private String coreStatus;
    private String externalReference;
    private OffsetDateTime chargedAt;

    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public Integer getBillableLines() { return billableLines; }
    public void setBillableLines(Integer billableLines) { this.billableLines = billableLines; }
    public BigDecimal getUnitFee() { return unitFee; }
    public void setUnitFee(BigDecimal unitFee) { this.unitFee = unitFee; }
    public BigDecimal getCommissionSubtotal() { return commissionSubtotal; }
    public void setCommissionSubtotal(BigDecimal commissionSubtotal) { this.commissionSubtotal = commissionSubtotal; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalCommission() { return totalCommission; }
    public void setTotalCommission(BigDecimal totalCommission) { this.totalCommission = totalCommission; }
    public String getBillingStatus() { return billingStatus; }
    public void setBillingStatus(String billingStatus) { this.billingStatus = billingStatus; }
    public String getCoreStatus() { return coreStatus; }
    public void setCoreStatus(String coreStatus) { this.coreStatus = coreStatus; }
    public String getExternalReference() { return externalReference; }
    public void setExternalReference(String externalReference) { this.externalReference = externalReference; }
    public OffsetDateTime getChargedAt() { return chargedAt; }
    public void setChargedAt(OffsetDateTime chargedAt) { this.chargedAt = chargedAt; }
}
