package com.banquito.switchpagos.reporting.mapper;

import com.banquito.switchpagos.reporting.dto.event.OffUsClearingIncludedEvent;
import com.banquito.switchpagos.reporting.dto.event.PaymentLineRoutedOffUsEvent;
import com.banquito.switchpagos.reporting.enums.ClearingFileStatus;
import com.banquito.switchpagos.reporting.enums.ClearingLineStatus;
import com.banquito.switchpagos.reporting.model.ClearingFile;
import com.banquito.switchpagos.reporting.model.ClearingFileLine;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class OffUsClearingMapper {

    private static final String SOURCE_SERVICE = "banquito-switch-reporting-service";

    public ClearingFile toNewClearingFile(PaymentLineRoutedOffUsEvent event, OffsetDateTime now) {
        ClearingFile clearingFile = new ClearingFile(UUID.randomUUID());
        clearingFile.setBatchId(event.getBatchId());
        clearingFile.setCorrelationId(event.getCorrelationId());
        clearingFile.setFileName(buildFileName(event.getBatchId()));
        clearingFile.setRelativePath("clearing/" + clearingFile.getFileName());
        clearingFile.setStatus(ClearingFileStatus.ABIERTO.name());
        clearingFile.setTotalLines(0);
        clearingFile.setTotalAmount(BigDecimal.ZERO);
        clearingFile.setCurrency(event.getCurrency());
        clearingFile.setCreatedAt(now);
        clearingFile.setUpdatedAt(now);
        return clearingFile;
    }

    public ClearingFileLine toLine(PaymentLineRoutedOffUsEvent event, ClearingFile clearingFile, OffsetDateTime now) {
        ClearingFileLine line = new ClearingFileLine(UUID.randomUUID());
        line.setClearingFileId(clearingFile.getClearingFileId());
        line.setSourceEventId(event.getEventId());
        line.setBatchId(event.getBatchId());
        line.setLineId(event.getLineId());
        line.setCorrelationId(event.getCorrelationId());
        line.setSequenceNumber(event.getSequenceNumber());
        line.setCompanyRuc(event.getCompanyRuc());
        line.setSourceAccountNumber(event.getSourceAccountNumber());
        line.setCoreFundingId(event.getCoreFundingId());
        line.setBeneficiaryIdentification(event.getBeneficiaryIdentification());
        line.setBeneficiaryName(event.getBeneficiaryName());
        line.setDestinationAccountNumber(event.getDestinationAccountNumber());
        line.setRoutingCode(event.getRoutingCode());
        line.setDestinationInstitutionName(event.getDestinationInstitutionName());
        line.setAmount(event.getAmount());
        line.setCurrency(event.getCurrency());
        line.setReference(event.getReference());
        line.setNotificationEmail(event.getNotificationEmail());
        line.setStatus(ClearingLineStatus.RECIBIDA.name());
        line.setBillable(Boolean.FALSE);
        line.setReceivedAt(now);
        line.setUpdatedAt(now);
        return line;
    }

    public void markIncluded(ClearingFileLine line, OffsetDateTime now) {
        line.setStatus(ClearingLineStatus.INCLUIDA_ARCHIVO_COMPENSACION.name());
        line.setBillable(Boolean.TRUE);
        line.setRejectionCode(null);
        line.setRejectionReason(null);
        line.setIncludedAt(now);
        line.setUpdatedAt(now);
    }

    public void markFailed(ClearingFileLine line, String errorCode, String errorReason, OffsetDateTime now) {
        line.setStatus(ClearingLineStatus.FALLIDA.name());
        line.setBillable(Boolean.FALSE);
        line.setRejectionCode(errorCode);
        line.setRejectionReason(errorReason);
        line.setUpdatedAt(now);
    }

    public void updateFileTotals(ClearingFile clearingFile, Integer totalLines, BigDecimal totalAmount, OffsetDateTime now) {
        clearingFile.setTotalLines(totalLines);
        clearingFile.setTotalAmount(totalAmount);
        clearingFile.setStatus(ClearingFileStatus.ACTUALIZADO.name());
        clearingFile.setUpdatedAt(now);
    }

    public void markFileFailed(ClearingFile clearingFile, OffsetDateTime now) {
        clearingFile.setStatus(ClearingFileStatus.FALLIDO.name());
        clearingFile.setUpdatedAt(now);
    }

    public OffUsClearingIncludedEvent toIncludedEvent(ClearingFile clearingFile, ClearingFileLine line, UUID eventId, OffsetDateTime now) {
        OffUsClearingIncludedEvent event = new OffUsClearingIncludedEvent();
        event.setEventId(eventId);
        event.setEventType("OFF_US_CLEARING_INCLUDED");
        event.setOccurredAt(now);
        event.setBatchId(line.getBatchId());
        event.setLineId(line.getLineId());
        event.setCorrelationId(line.getCorrelationId());
        event.setSourceService(SOURCE_SERVICE);
        event.setFinalStatus(line.getStatus());
        event.setBillable(line.getBillable());
        event.setAmount(line.getAmount());
        event.setCurrency(line.getCurrency());
        event.setClearingFileId(clearingFile.getClearingFileId());
        event.setClearingFileName(clearingFile.getFileName());
        event.setNotificationEmail(line.getNotificationEmail());
        return event;
    }

    private String buildFileName(UUID batchId) {
        String compactBatchId = batchId.toString().replace("-", "");
        return "clearing_" + compactBatchId.substring(0, 12) + ".csv";
    }
}
