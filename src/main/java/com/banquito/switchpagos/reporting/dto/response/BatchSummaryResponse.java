package com.banquito.switchpagos.reporting.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BatchSummaryResponse {

    private UUID batchId;
    private UUID correlationId;
    private String companyRuc;
    private String sourceAccountNumber;
    private String coreFundingId;
    private Integer totalLines;
    private Integer expectedTotalLines;
    private Integer observedLines;
    private Integer finalResultLines;
    private Integer onUsCreditedLines;
    private Integer offUsIncludedLines;
    private Integer rejectedLines;
    private Integer failedLines;
    private Integer billableLines;
    private BigDecimal controlAmount;
    private BigDecimal processedAmount;
    private BigDecimal remainingAmount;
    private BigDecimal unprocessedAmount;
    private Boolean unprocessedAmountReturned;
    private String unprocessedAmountPolicy;
    private String currency;
    private String status;
    private String billingStatus;
    private BigDecimal commissionSubtotal;
    private BigDecimal totalChargedAmount;
    private OffsetDateTime generatedAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime updatedAt;

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
    public Integer getTotalLines() { return totalLines; }
    public void setTotalLines(Integer totalLines) { this.totalLines = totalLines; }
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
    public BigDecimal getUnprocessedAmount() { return unprocessedAmount; }
    public void setUnprocessedAmount(BigDecimal unprocessedAmount) { this.unprocessedAmount = unprocessedAmount; }
    public Boolean getUnprocessedAmountReturned() { return unprocessedAmountReturned; }
    public void setUnprocessedAmountReturned(Boolean unprocessedAmountReturned) { this.unprocessedAmountReturned = unprocessedAmountReturned; }
    public String getUnprocessedAmountPolicy() { return unprocessedAmountPolicy; }
    public void setUnprocessedAmountPolicy(String unprocessedAmountPolicy) { this.unprocessedAmountPolicy = unprocessedAmountPolicy; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBillingStatus() { return billingStatus; }
    public void setBillingStatus(String billingStatus) { this.billingStatus = billingStatus; }
    public BigDecimal getCommissionSubtotal() { return commissionSubtotal; }
    public void setCommissionSubtotal(BigDecimal commissionSubtotal) { this.commissionSubtotal = commissionSubtotal; }
    public BigDecimal getTotalChargedAmount() { return totalChargedAmount; }
    public void setTotalChargedAmount(BigDecimal totalChargedAmount) { this.totalChargedAmount = totalChargedAmount; }
    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
