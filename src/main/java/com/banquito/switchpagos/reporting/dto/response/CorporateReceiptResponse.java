package com.banquito.switchpagos.reporting.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CorporateReceiptResponse {

    private UUID batchId;
    private UUID receiptId;
    private String fileName;
    private String filePath;
    private String content;
    private OffsetDateTime generatedAt;
    private String companyRuc;
    private String sourceAccountNumber;
    private Integer totalReceivedLines;
    private Integer successfulOnUsLines;
    private Integer includedOffUsLines;
    private Integer rejectedLines;
    private Integer failedLines;
    private Integer billableLines;
    private BigDecimal totalControlAmount;
    private BigDecimal fundedAmount;
    private BigDecimal totalProcessedAmount;
    private BigDecimal remainingAmount;
    private BigDecimal unprocessedAmount;
    private Boolean unprocessedAmountReturned;
    private String unprocessedAmountPolicy;
    /**
     * Legacy compatibility only. The current business flow does not release or return unprocessed amount.
     */
    @Deprecated
    private BigDecimal releasedAmount;
    private BigDecimal unitFee;
    private BigDecimal commissionSubtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalChargedAmount;
    private String currency;
    private String billingStatus;
    private String coreCommissionChargeId;
    private String coreFundingId;
    private String reservationUuid;
    private String clearingFileName;
    private String clearingFilePath;

    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getReceiptId() { return receiptId; }
    public void setReceiptId(UUID receiptId) { this.receiptId = receiptId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; }
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
    public BigDecimal getFundedAmount() { return fundedAmount; }
    public void setFundedAmount(BigDecimal fundedAmount) { this.fundedAmount = fundedAmount; }
    public BigDecimal getTotalProcessedAmount() { return totalProcessedAmount; }
    public void setTotalProcessedAmount(BigDecimal totalProcessedAmount) { this.totalProcessedAmount = totalProcessedAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public BigDecimal getUnprocessedAmount() { return unprocessedAmount; }
    public void setUnprocessedAmount(BigDecimal unprocessedAmount) { this.unprocessedAmount = unprocessedAmount; }
    public Boolean getUnprocessedAmountReturned() { return unprocessedAmountReturned; }
    public void setUnprocessedAmountReturned(Boolean unprocessedAmountReturned) { this.unprocessedAmountReturned = unprocessedAmountReturned; }
    public String getUnprocessedAmountPolicy() { return unprocessedAmountPolicy; }
    public void setUnprocessedAmountPolicy(String unprocessedAmountPolicy) { this.unprocessedAmountPolicy = unprocessedAmountPolicy; }
    @Deprecated
    public BigDecimal getReleasedAmount() { return releasedAmount; }
    @Deprecated
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
    public String getCoreFundingId() { return coreFundingId; }
    public void setCoreFundingId(String coreFundingId) { this.coreFundingId = coreFundingId; }
    public String getReservationUuid() { return reservationUuid; }
    public void setReservationUuid(String reservationUuid) { this.reservationUuid = reservationUuid; }
    public String getClearingFileName() { return clearingFileName; }
    public void setClearingFileName(String clearingFileName) { this.clearingFileName = clearingFileName; }
    public String getClearingFilePath() { return clearingFilePath; }
    public void setClearingFilePath(String clearingFilePath) { this.clearingFilePath = clearingFilePath; }
}
