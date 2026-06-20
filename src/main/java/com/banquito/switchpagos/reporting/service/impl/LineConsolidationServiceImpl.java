package com.banquito.switchpagos.reporting.service.impl;

import com.banquito.switchpagos.reporting.dto.event.BatchLinesCompletedEvent;
import com.banquito.switchpagos.reporting.dto.event.OffUsClearingIncludedEvent;
import com.banquito.switchpagos.reporting.dto.event.OnUsSettlementCompletedEvent;
import com.banquito.switchpagos.reporting.dto.event.PaymentLineRejectedEvent;
import com.banquito.switchpagos.reporting.dto.event.PaymentLineRequestedEvent;
import com.banquito.switchpagos.reporting.enums.BatchConsolidationStatus;
import com.banquito.switchpagos.reporting.enums.ClearingLineStatus;
import com.banquito.switchpagos.reporting.model.BatchLineObservation;
import com.banquito.switchpagos.reporting.model.BatchProcessingSummary;
import com.banquito.switchpagos.reporting.model.ClearingFileLine;
import com.banquito.switchpagos.reporting.model.LineProcessingResult;
import com.banquito.switchpagos.reporting.repository.BatchLineObservationRepository;
import com.banquito.switchpagos.reporting.repository.BatchProcessingSummaryRepository;
import com.banquito.switchpagos.reporting.repository.LineProcessingResultRepository;
import com.banquito.switchpagos.reporting.service.BatchLinesCompletedEventPublisher;
import com.banquito.switchpagos.reporting.service.LineConsolidationService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineConsolidationServiceImpl implements LineConsolidationService {

    private static final Logger LOG = LoggerFactory.getLogger(LineConsolidationServiceImpl.class);
    private static final String STATUS_ACREDITADA_ON_US = "ACREDITADA_ON_US";
    private static final String STATUS_INCLUIDA_ARCHIVO_COMPENSACION = "INCLUIDA_ARCHIVO_COMPENSACION";
    private static final String STATUS_RECHAZADA = "RECHAZADA";
    private static final String STATUS_FALLIDA = "FALLIDA";
    private static final String SOURCE_SERVICE = "banquito-switch-reporting-service";

    private final BatchLineObservationRepository observationRepository;
    private final LineProcessingResultRepository resultRepository;
    private final BatchProcessingSummaryRepository summaryRepository;
    private final BatchLinesCompletedEventPublisher completedEventPublisher;

    public LineConsolidationServiceImpl(
            BatchLineObservationRepository observationRepository,
            LineProcessingResultRepository resultRepository,
            BatchProcessingSummaryRepository summaryRepository,
            BatchLinesCompletedEventPublisher completedEventPublisher) {
        this.observationRepository = observationRepository;
        this.resultRepository = resultRepository;
        this.summaryRepository = summaryRepository;
        this.completedEventPublisher = completedEventPublisher;
    }

    @Override
    @Transactional
    public synchronized void observePaymentLine(PaymentLineRequestedEvent event) {
        validateCommon(event.getEventId(), event.getBatchId(), event.getLineId(), event.getCorrelationId(), "PaymentLineRequestedEvent");
        if (observationRepository.findByLineId(event.getLineId()).isEmpty()) {
            BatchLineObservation observation = new BatchLineObservation();
            OffsetDateTime now = OffsetDateTime.now();
            observation.setBatchLineObservationId(UUID.randomUUID());
            observation.setSourceEventId(event.getEventId());
            observation.setBatchId(event.getBatchId());
            observation.setLineId(event.getLineId());
            observation.setCorrelationId(event.getCorrelationId());
            observation.setSequenceNumber(event.getSequenceNumber());
            observation.setCompanyRuc(event.getCompanyRuc());
            observation.setSourceAccountNumber(event.getSourceAccountNumber());
            observation.setCoreFundingId(event.getCoreFundingId());
            observation.setAmount(requireAmount(event.getAmount()));
            observation.setCurrency(requireCurrency(event.getCurrency()));
            observation.setBatchTotalLines(event.getBatchTotalLines());
            observation.setBatchControlAmount(event.getBatchControlAmount());
            observation.setBeneficiaryIdentification(limitText(event.getBeneficiaryIdentification(), 30));
            observation.setBeneficiaryName(limitText(event.getBeneficiaryName(), 120));
            observation.setDestinationAccountNumber(limitText(event.getDestinationAccountNumber(), 40));
            observation.setRoutingCode(limitText(event.getRoutingCode(), 10));
            observation.setReference(limitText(event.getReference(), 140));
            observation.setNotificationEmail(limitText(event.getNotificationEmail(), 120));
            observation.setObservedAt(now);
            observation.setUpdatedAt(now);
            observationRepository.save(observation);
            LOG.info("Linea observada para consolidacion. batchId={}, lineId={}", event.getBatchId(), event.getLineId());
        }
        BatchProcessingSummary summary = getOrCreateSummary(
                event.getBatchId(),
                event.getCorrelationId(),
                event.getCurrency(),
                OffsetDateTime.now());
        mergeExpectedData(
                summary,
                event.getBatchTotalLines(),
                event.getBatchControlAmount(),
                event.getCurrency(),
                event.getCompanyRuc(),
                event.getSourceAccountNumber(),
                event.getCoreFundingId());
        refreshAndPublishIfComplete(summary);
    }

    @Override
    @Transactional
    public synchronized void registerOnUsCompleted(OnUsSettlementCompletedEvent event) {
        validateCommon(event.getEventId(), event.getBatchId(), event.getLineId(), event.getCorrelationId(), "OnUsSettlementCompletedEvent");
        registerResult(
                event.getEventId(),
                event.getBatchId(),
                event.getLineId(),
                event.getCorrelationId(),
                normalizeFinalStatus(event.getFinalStatus()),
                Boolean.TRUE.equals(event.getBillable()),
                event.getAmount(),
                event.getCurrency(),
                "ON_US_SETTLEMENT",
                event.getCoreTransactionId(),
                null,
                null,
                event.getRejectionCode(),
                event.getRejectionReason(),
                event.getNotificationEmail());
    }

    @Override
    @Transactional
    public synchronized void registerOffUsIncluded(OffUsClearingIncludedEvent event) {
        validateCommon(event.getEventId(), event.getBatchId(), event.getLineId(), event.getCorrelationId(), "OffUsClearingIncludedEvent");
        registerResult(
                event.getEventId(),
                event.getBatchId(),
                event.getLineId(),
                event.getCorrelationId(),
                STATUS_INCLUIDA_ARCHIVO_COMPENSACION,
                Boolean.TRUE.equals(event.getBillable()),
                event.getAmount(),
                event.getCurrency(),
                "OFF_US_CLEARING",
                null,
                event.getClearingFileId(),
                event.getClearingFileName(),
                null,
                null,
                event.getNotificationEmail());
    }

    @Override
    @Transactional
    public synchronized void registerOffUsFailure(ClearingFileLine line) {
        registerResult(
                line.getSourceEventId(),
                line.getBatchId(),
                line.getLineId(),
                line.getCorrelationId(),
                STATUS_FALLIDA,
                false,
                line.getAmount(),
                line.getCurrency(),
                "OFF_US_CLEARING",
                null,
                null,
                null,
                line.getRejectionCode(),
                line.getRejectionReason(),
                line.getNotificationEmail());
    }

    @Override
    @Transactional
    public synchronized void registerRejected(PaymentLineRejectedEvent event) {
        validateCommon(event.getEventId(), event.getBatchId(), event.getLineId(), event.getCorrelationId(), "PaymentLineRejectedEvent");
        registerResult(
                event.getEventId(),
                event.getBatchId(),
                event.getLineId(),
                event.getCorrelationId(),
                STATUS_RECHAZADA,
                false,
                event.getAmount(),
                event.getCurrency(),
                "LINE_REJECTED",
                null,
                null,
                null,
                event.getRejectionCode(),
                event.getRejectionReason(),
                null);
    }

    private void registerResult(
            UUID sourceEventId,
            UUID batchId,
            UUID lineId,
            UUID correlationId,
            String finalStatus,
            Boolean billable,
            BigDecimal amount,
            String currency,
            String resultSource,
            String coreTransactionId,
            UUID clearingFileId,
            String clearingFileName,
            String rejectionCode,
            String rejectionReason,
            String notificationEmail) {
        if (resultRepository.findByLineId(lineId).isPresent()) {
            LOG.info("Resultado final duplicado ignorado. batchId={}, lineId={}, status={}", batchId, lineId, finalStatus);
            return;
        }
        OffsetDateTime now = OffsetDateTime.now();
        LineProcessingResult result = new LineProcessingResult();
        result.setLineProcessingResultId(UUID.randomUUID());
        result.setSourceEventId(sourceEventId);
        result.setBatchId(batchId);
        result.setLineId(lineId);
        result.setCorrelationId(correlationId);
        result.setFinalStatus(finalStatus);
        result.setBillable(billable);
        result.setAmount(requireAmount(amount));
        result.setCurrency(requireCurrency(currency));
        result.setResultSource(resultSource);
        result.setCoreTransactionId(coreTransactionId);
        result.setClearingFileId(clearingFileId);
        result.setClearingFileName(clearingFileName);
        result.setRejectionCode(limitText(rejectionCode, 80));
        result.setRejectionReason(limitText(rejectionReason, 500));
        result.setNotificationEmail(limitText(notificationEmail, 120));
        result.setReceivedAt(now);
        result.setUpdatedAt(now);
        resultRepository.save(result);

        BatchProcessingSummary summary = getOrCreateSummary(batchId, correlationId, currency, now);
        refreshAndPublishIfComplete(summary);
        LOG.info("Resultado final registrado para consolidacion. batchId={}, lineId={}, status={}", batchId, lineId, finalStatus);
    }

    private BatchProcessingSummary getOrCreateSummary(UUID batchId, UUID correlationId, String currency, OffsetDateTime now) {
        return summaryRepository.findByBatchId(batchId).orElseGet(() -> {
            BatchProcessingSummary summary = new BatchProcessingSummary();
            summary.setBatchProcessingSummaryId(UUID.randomUUID());
            summary.setBatchId(batchId);
            summary.setCorrelationId(correlationId);
            summary.setExpectedTotalLines(null);
            summary.setObservedLines(0);
            summary.setFinalResultLines(0);
            summary.setOnUsCreditedLines(0);
            summary.setOffUsIncludedLines(0);
            summary.setRejectedLines(0);
            summary.setFailedLines(0);
            summary.setBillableLines(0);
            summary.setControlAmount(null);
            summary.setProcessedAmount(BigDecimal.ZERO);
            summary.setRemainingAmount(null);
            summary.setCurrency(requireCurrency(currency));
            summary.setStatus(BatchConsolidationStatus.EN_OBSERVACION.name());
            summary.setCompletedEventPublished(false);
            summary.setFirstObservedAt(now);
            summary.setUpdatedAt(now);
            return summaryRepository.save(summary);
        });
    }

    private void mergeExpectedData(
            BatchProcessingSummary summary,
            Integer expectedTotalLines,
            BigDecimal controlAmount,
            String currency,
            String companyRuc,
            String sourceAccountNumber,
            String coreFundingId) {
        if (expectedTotalLines != null) {
            summary.setExpectedTotalLines(expectedTotalLines);
        }
        if (controlAmount != null) {
            summary.setControlAmount(controlAmount);
        }
        if (currency != null && !currency.isBlank()) {
            summary.setCurrency(currency);
        }
        if (companyRuc != null && !companyRuc.isBlank()) {
            summary.setCompanyRuc(companyRuc);
        }
        if (sourceAccountNumber != null && !sourceAccountNumber.isBlank()) {
            summary.setSourceAccountNumber(sourceAccountNumber);
        }
        if (coreFundingId != null && !coreFundingId.isBlank()) {
            summary.setCoreFundingId(coreFundingId);
        }
        summary.setUpdatedAt(OffsetDateTime.now());
        summaryRepository.save(summary);
    }

    private void refreshAndPublishIfComplete(BatchProcessingSummary summary) {
        List<LineProcessingResult> results = resultRepository.findByBatchId(summary.getBatchId());
        Integer observedLines = observationRepository.countByBatchId(summary.getBatchId()).intValue();
        Integer finalResultLines = results.size();
        Integer onUsCreditedLines = 0;
        Integer offUsIncludedLines = 0;
        Integer rejectedLines = 0;
        Integer failedLines = 0;
        Integer billableLines = 0;
        BigDecimal processedAmount = BigDecimal.ZERO;

        for (LineProcessingResult result : results) {
            if (STATUS_ACREDITADA_ON_US.equals(result.getFinalStatus())) {
                onUsCreditedLines++;
            } else if (STATUS_INCLUIDA_ARCHIVO_COMPENSACION.equals(result.getFinalStatus())) {
                offUsIncludedLines++;
            } else if (STATUS_RECHAZADA.equals(result.getFinalStatus())) {
                rejectedLines++;
            } else if (STATUS_FALLIDA.equals(result.getFinalStatus())) {
                failedLines++;
            }
            if (Boolean.TRUE.equals(result.getBillable())) {
                billableLines++;
                processedAmount = processedAmount.add(result.getAmount());
            }
        }

        summary.setObservedLines(observedLines);
        summary.setFinalResultLines(finalResultLines);
        summary.setOnUsCreditedLines(onUsCreditedLines);
        summary.setOffUsIncludedLines(offUsIncludedLines);
        summary.setRejectedLines(rejectedLines);
        summary.setFailedLines(failedLines);
        summary.setBillableLines(billableLines);
        summary.setProcessedAmount(processedAmount);
        summary.setRemainingAmount(summary.getControlAmount() == null ? null : summary.getControlAmount().subtract(processedAmount));
        summary.setUpdatedAt(OffsetDateTime.now());

        Boolean complete = summary.getExpectedTotalLines() != null
                && summary.getExpectedTotalLines().equals(finalResultLines)
                && Boolean.FALSE.equals(summary.getCompletedEventPublished());
        if (!complete) {
            summaryRepository.save(summary);
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        UUID completedEventId = UUID.randomUUID();
        summary.setStatus(BatchConsolidationStatus.COMPLETADO.name());
        summary.setCompletedAt(now);
        summary.setCompletedEventId(completedEventId);
        summaryRepository.save(summary);

        BatchLinesCompletedEvent completedEvent = toCompletedEvent(summary, completedEventId, now);
        completedEventPublisher.publish(completedEvent);
        summary.setCompletedEventPublished(true);
        summary.setUpdatedAt(OffsetDateTime.now());
        summaryRepository.save(summary);
        LOG.info("BatchLinesCompletedEvent publicado. batchId={}, eventId={}, totalLines={}",
                summary.getBatchId(), completedEventId, summary.getExpectedTotalLines());
    }

    private BatchLinesCompletedEvent toCompletedEvent(
            BatchProcessingSummary summary,
            UUID eventId,
            OffsetDateTime occurredAt) {
        BatchLinesCompletedEvent event = new BatchLinesCompletedEvent();
        event.setEventId(eventId);
        event.setEventType("BATCH_LINES_COMPLETED");
        event.setOccurredAt(occurredAt);
        event.setBatchId(summary.getBatchId());
        event.setCorrelationId(summary.getCorrelationId());
        event.setSourceService(SOURCE_SERVICE);
        event.setCompanyRuc(summary.getCompanyRuc());
        event.setSourceAccountNumber(summary.getSourceAccountNumber());
        event.setCoreFundingId(summary.getCoreFundingId());
        event.setTotalLines(summary.getExpectedTotalLines());
        event.setOnUsCreditedLines(summary.getOnUsCreditedLines());
        event.setOffUsIncludedLines(summary.getOffUsIncludedLines());
        event.setRejectedLines(summary.getRejectedLines());
        event.setFailedLines(summary.getFailedLines());
        event.setBillableLines(summary.getBillableLines());
        event.setControlAmount(summary.getControlAmount());
        event.setProcessedAmount(summary.getProcessedAmount());
        event.setRemainingAmount(summary.getRemainingAmount());
        event.setCurrency(summary.getCurrency());
        return event;
    }

    private void validateCommon(UUID eventId, UUID batchId, UUID lineId, UUID correlationId, String eventName) {
        if (eventId == null || batchId == null || lineId == null || correlationId == null) {
            throw new IllegalArgumentException(eventName + " debe incluir eventId, batchId, lineId y correlationId");
        }
    }

    private String normalizeFinalStatus(String finalStatus) {
        if (ClearingLineStatus.FALLIDA.name().equals(finalStatus)) {
            return STATUS_FALLIDA;
        }
        if (finalStatus == null || finalStatus.isBlank()) {
            return STATUS_FALLIDA;
        }
        return finalStatus;
    }

    private BigDecimal requireAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    private String requireCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            return "USD";
        }
        return currency;
    }

    private String limitText(String text, Integer maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }
}
