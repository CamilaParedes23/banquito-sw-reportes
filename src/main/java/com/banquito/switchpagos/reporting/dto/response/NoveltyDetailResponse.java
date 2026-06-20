package com.banquito.switchpagos.reporting.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class NoveltyDetailResponse {

    private UUID lineId;
    private Integer sequenceNumber;
    private String beneficiaryIdentification;
    private String beneficiaryName;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private String currency;
    private String finalStatus;
    private String noveltyType;
    private String errorCode;
    private String errorMessage;
    private String errorSource;
    private OffsetDateTime processedAt;

    public UUID getLineId() { return lineId; }
    public void setLineId(UUID lineId) { this.lineId = lineId; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public String getBeneficiaryIdentification() { return beneficiaryIdentification; }
    public void setBeneficiaryIdentification(String beneficiaryIdentification) { this.beneficiaryIdentification = beneficiaryIdentification; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getFinalStatus() { return finalStatus; }
    public void setFinalStatus(String finalStatus) { this.finalStatus = finalStatus; }
    public String getNoveltyType() { return noveltyType; }
    public void setNoveltyType(String noveltyType) { this.noveltyType = noveltyType; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getErrorSource() { return errorSource; }
    public void setErrorSource(String errorSource) { this.errorSource = errorSource; }
    public OffsetDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(OffsetDateTime processedAt) { this.processedAt = processedAt; }
}
