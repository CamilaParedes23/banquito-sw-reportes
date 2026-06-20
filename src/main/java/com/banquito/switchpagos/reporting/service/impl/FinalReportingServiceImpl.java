package com.banquito.switchpagos.reporting.service.impl;

import com.banquito.switchpagos.reporting.client.NotificationClient;
import com.banquito.switchpagos.reporting.client.NotificationClientResult;
import com.banquito.switchpagos.reporting.dto.event.BillingCompletedEvent;
import com.banquito.switchpagos.reporting.dto.request.NotificationRequest;
import com.banquito.switchpagos.reporting.enums.FinalReportingStatus;
import com.banquito.switchpagos.reporting.enums.GeneratedDocumentType;
import com.banquito.switchpagos.reporting.enums.NotificationOrderStatus;
import com.banquito.switchpagos.reporting.file.FinalReportingFileWriter;
import com.banquito.switchpagos.reporting.model.BatchLineObservation;
import com.banquito.switchpagos.reporting.model.BatchProcessingSummary;
import com.banquito.switchpagos.reporting.model.ClearingFile;
import com.banquito.switchpagos.reporting.model.CorporateReceipt;
import com.banquito.switchpagos.reporting.model.GeneratedDocument;
import com.banquito.switchpagos.reporting.model.LineProcessingResult;
import com.banquito.switchpagos.reporting.model.NotificationOrder;
import com.banquito.switchpagos.reporting.model.NoveltyReport;
import com.banquito.switchpagos.reporting.model.NoveltyReportLine;
import com.banquito.switchpagos.reporting.repository.BatchLineObservationRepository;
import com.banquito.switchpagos.reporting.repository.BatchProcessingSummaryRepository;
import com.banquito.switchpagos.reporting.repository.ClearingFileRepository;
import com.banquito.switchpagos.reporting.repository.CorporateReceiptRepository;
import com.banquito.switchpagos.reporting.repository.GeneratedDocumentRepository;
import com.banquito.switchpagos.reporting.repository.LineProcessingResultRepository;
import com.banquito.switchpagos.reporting.repository.NotificationOrderRepository;
import com.banquito.switchpagos.reporting.repository.NoveltyReportLineRepository;
import com.banquito.switchpagos.reporting.repository.NoveltyReportRepository;
import com.banquito.switchpagos.reporting.service.FinalReportingService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinalReportingServiceImpl implements FinalReportingService {

    private static final Logger LOG = LoggerFactory.getLogger(FinalReportingServiceImpl.class);
    private static final String STATUS_ACREDITADA_ON_US = "ACREDITADA_ON_US";
    private static final String STATUS_INCLUIDA_ARCHIVO_COMPENSACION = "INCLUIDA_ARCHIVO_COMPENSACION";
    private static final String STATUS_RECHAZADA = "RECHAZADA";
    private static final String STATUS_FALLIDA = "FALLIDA";
    private static final String ON_US_NOTIFICATION_MESSAGE =
            "Su pago fue acreditado en una cuenta Banco BanQuito.";
    private static final String OFF_US_NOTIFICATION_MESSAGE =
            "Su pago fue incluido en el archivo de compensacion interbancaria.";

    private final BatchProcessingSummaryRepository summaryRepository;
    private final BatchLineObservationRepository observationRepository;
    private final LineProcessingResultRepository resultRepository;
    private final NoveltyReportRepository noveltyReportRepository;
    private final NoveltyReportLineRepository noveltyReportLineRepository;
    private final CorporateReceiptRepository corporateReceiptRepository;
    private final ClearingFileRepository clearingFileRepository;
    private final NotificationOrderRepository notificationOrderRepository;
    private final GeneratedDocumentRepository generatedDocumentRepository;
    private final FinalReportingFileWriter fileWriter;
    private final NotificationClient notificationClient;
    private final String notificationOriginService;
    private final String notificationOnUsEventType;
    private final String notificationOffUsEventType;
    private final String notificationPriority;
    private final String notificationChannelType;
    private final String notificationSubject;

    public FinalReportingServiceImpl(
            BatchProcessingSummaryRepository summaryRepository,
            BatchLineObservationRepository observationRepository,
            LineProcessingResultRepository resultRepository,
            NoveltyReportRepository noveltyReportRepository,
            NoveltyReportLineRepository noveltyReportLineRepository,
            CorporateReceiptRepository corporateReceiptRepository,
            ClearingFileRepository clearingFileRepository,
            NotificationOrderRepository notificationOrderRepository,
            GeneratedDocumentRepository generatedDocumentRepository,
            FinalReportingFileWriter fileWriter,
            NotificationClient notificationClient,
            @Value("${notification.switch.origin-service}") String notificationOriginService,
            @Value("${notification.switch.event-type-on-us}") String notificationOnUsEventType,
            @Value("${notification.switch.event-type-off-us}") String notificationOffUsEventType,
            @Value("${notification.switch.priority}") String notificationPriority,
            @Value("${notification.switch.channel-type}") String notificationChannelType,
            @Value("${notification.switch.subject}") String notificationSubject) {
        this.summaryRepository = summaryRepository;
        this.observationRepository = observationRepository;
        this.resultRepository = resultRepository;
        this.noveltyReportRepository = noveltyReportRepository;
        this.noveltyReportLineRepository = noveltyReportLineRepository;
        this.corporateReceiptRepository = corporateReceiptRepository;
        this.clearingFileRepository = clearingFileRepository;
        this.notificationOrderRepository = notificationOrderRepository;
        this.generatedDocumentRepository = generatedDocumentRepository;
        this.fileWriter = fileWriter;
        this.notificationClient = notificationClient;
        this.notificationOriginService = notificationOriginService;
        this.notificationOnUsEventType = notificationOnUsEventType;
        this.notificationOffUsEventType = notificationOffUsEventType;
        this.notificationPriority = notificationPriority;
        this.notificationChannelType = notificationChannelType;
        this.notificationSubject = notificationSubject;
    }

    @Override
    @Transactional
    public synchronized void processBillingCompleted(BillingCompletedEvent event) {
        validateEvent(event);
        if (Boolean.TRUE.equals(noveltyReportRepository.existsByBatchId(event.getBatchId()))
                || Boolean.TRUE.equals(corporateReceiptRepository.existsByBatchId(event.getBatchId()))) {
            LOG.info("BillingCompletedEvent duplicado ignorado por reporting final. batchId={}", event.getBatchId());
            return;
        }

        BatchProcessingSummary summary = summaryRepository.findByBatchId(event.getBatchId()).orElse(null);
        if (summary == null) {
            LOG.warn("BillingCompletedEvent sin resumen local ignorado. batchId={}", event.getBatchId());
            return;
        }
        List<LineProcessingResult> results = resultRepository.findByBatchId(event.getBatchId());
        List<BatchLineObservation> observations = observationRepository.findByBatchId(event.getBatchId());
        if (results.isEmpty()) {
            LOG.warn("BillingCompletedEvent sin resultados locales de lineas ignorado. batchId={}", event.getBatchId());
            return;
        }

        Map<UUID, BatchLineObservation> observationByLineId = indexObservations(observations);
        OffsetDateTime now = OffsetDateTime.now();
        NoveltyReport noveltyReport = createNoveltyReport(event, results.size(), now);
        List<NoveltyReportLine> reportLines = createNoveltyReportLines(noveltyReport, results, observationByLineId, now);
        noveltyReportRepository.save(noveltyReport);
        noveltyReportLineRepository.saveAll(reportLines);
        fileWriter.writeNoveltyReport(noveltyReport.getFileName(), reportLines);
        registerGeneratedDocument(event.getBatchId(), GeneratedDocumentType.NOVELTY_REPORT.name(),
                noveltyReport.getNoveltyReportId(), noveltyReport.getFileName(), noveltyReport.getFilePath(), now);

        CorporateReceipt receipt = createCorporateReceipt(event, summary, noveltyReport, now);
        corporateReceiptRepository.save(receipt);
        fileWriter.writeCorporateReceipt(receipt);
        registerGeneratedDocument(event.getBatchId(), GeneratedDocumentType.CORPORATE_RECEIPT.name(),
                receipt.getCorporateReceiptId(), receipt.getFileName(), receipt.getFilePath(), now);

        List<NotificationOrder> orders = createNotificationOrders(results, observationByLineId, now);
        notificationOrderRepository.saveAll(orders);
        LOG.info("Reporting final generado. batchId={}, noveltyReport={}, corporateReceipt={}, notificationOrders={}",
                event.getBatchId(), noveltyReport.getFileName(), receipt.getFileName(), orders.size());
    }

    private NoveltyReport createNoveltyReport(BillingCompletedEvent event, Integer totalLines, OffsetDateTime now) {
        String fileName = "novelties_" + event.getBatchId() + ".csv";
        NoveltyReport report = new NoveltyReport();
        report.setNoveltyReportId(UUID.randomUUID());
        report.setSourceEventId(event.getEventId());
        report.setBatchId(event.getBatchId());
        report.setBillingId(event.getBillingId());
        report.setCorrelationId(event.getCorrelationId());
        report.setStatus(FinalReportingStatus.GENERADO.name());
        report.setFileName(fileName);
        report.setFilePath("reports/" + fileName);
        report.setTotalLines(totalLines);
        report.setGeneratedAt(now);
        report.setCreatedAt(now);
        report.setUpdatedAt(now);
        return report;
    }

    private List<NoveltyReportLine> createNoveltyReportLines(
            NoveltyReport report,
            List<LineProcessingResult> results,
            Map<UUID, BatchLineObservation> observationByLineId,
            OffsetDateTime now) {
        List<LineProcessingResult> sortedResults = new ArrayList<>(results);
        sortedResults.sort(Comparator
                .comparing((LineProcessingResult result) -> sequenceOf(result, observationByLineId), Comparator.nullsLast(Integer::compareTo))
                .thenComparing(LineProcessingResult::getLineId));
        List<NoveltyReportLine> reportLines = new ArrayList<>();
        for (LineProcessingResult result : sortedResults) {
            BatchLineObservation observation = observationByLineId.get(result.getLineId());
            NoveltyReportLine line = new NoveltyReportLine();
            line.setNoveltyReportLineId(UUID.randomUUID());
            line.setNoveltyReportId(report.getNoveltyReportId());
            line.setBatchId(result.getBatchId());
            line.setLineId(result.getLineId());
            line.setSequenceNumber(observation == null ? null : observation.getSequenceNumber());
            line.setRouteType(resolveRouteType(result, observation));
            line.setBeneficiaryIdentification(observation == null ? null : observation.getBeneficiaryIdentification());
            line.setBeneficiaryName(observation == null ? null : observation.getBeneficiaryName());
            line.setDestinationAccountNumber(observation == null ? null : observation.getDestinationAccountNumber());
            line.setRoutingCode(observation == null ? null : observation.getRoutingCode());
            line.setAmount(result.getAmount());
            line.setCurrency(result.getCurrency());
            line.setFinalStatus(result.getFinalStatus());
            line.setBillable(result.getBillable());
            line.setRejectionCode(result.getRejectionCode());
            line.setRejectionReason(result.getRejectionReason());
            line.setMessage(resolveMessage(result));
            line.setCreatedAt(now);
            reportLines.add(line);
        }
        return reportLines;
    }

    private CorporateReceipt createCorporateReceipt(
            BillingCompletedEvent event,
            BatchProcessingSummary summary,
            NoveltyReport noveltyReport,
            OffsetDateTime now) {
        String fileName = "corporate_receipt_" + event.getBatchId() + ".json";
        CorporateReceipt receipt = new CorporateReceipt();
        receipt.setCorporateReceiptId(UUID.randomUUID());
        receipt.setSourceEventId(event.getEventId());
        receipt.setBatchId(event.getBatchId());
        receipt.setBillingId(event.getBillingId());
        receipt.setCorrelationId(event.getCorrelationId());
        receipt.setCompanyRuc(summary.getCompanyRuc());
        receipt.setSourceAccountNumber(summary.getSourceAccountNumber());
        receipt.setTotalReceivedLines(defaultInteger(summary.getExpectedTotalLines()));
        receipt.setSuccessfulOnUsLines(defaultInteger(summary.getOnUsCreditedLines()));
        receipt.setIncludedOffUsLines(defaultInteger(summary.getOffUsIncludedLines()));
        receipt.setRejectedLines(defaultInteger(summary.getRejectedLines()));
        receipt.setFailedLines(defaultInteger(summary.getFailedLines()));
        receipt.setBillableLines(defaultInteger(event.getBillableLines()));
        receipt.setTotalControlAmount(summary.getControlAmount());
        receipt.setFundedAmount(summary.getControlAmount());
        receipt.setTotalProcessedAmount(defaultAmount(summary.getProcessedAmount()));
        receipt.setRemainingAmount(defaultAmount(
                event.getRemainingAmount() == null ? summary.getRemainingAmount() : event.getRemainingAmount()));
        receipt.setFundingAdjustmentStatus(null);
        receipt.setReleasedAmount(null);
        receipt.setUnitFee(event.getUnitFee());
        receipt.setCommissionSubtotal(event.getCommissionSubtotal());
        receipt.setTaxAmount(event.getTaxAmount());
        receipt.setTotalChargedAmount(event.getTotalChargedAmount());
        receipt.setCurrency(requireCurrency(event.getCurrency(), summary.getCurrency()));
        receipt.setBillingStatus(event.getBillingStatus());
        receipt.setCoreCommissionChargeId(event.getCoreCommissionChargeId());
        receipt.setCoreFundingId(summary.getCoreFundingId());
        receipt.setFundingReleaseCoreTransactionId(null);
        receipt.setNoveltyReportId(noveltyReport.getNoveltyReportId());
        receipt.setNoveltyReportFilePath(noveltyReport.getFilePath());
        clearingFileRepository.findByBatchId(event.getBatchId()).ifPresent(clearingFile -> applyClearingTrace(receipt, clearingFile));
        receipt.setFileName(fileName);
        receipt.setFilePath("receipts/" + fileName);
        receipt.setGeneratedAt(now);
        receipt.setCreatedAt(now);
        receipt.setUpdatedAt(now);
        return receipt;
    }

    private void applyClearingTrace(CorporateReceipt receipt, ClearingFile clearingFile) {
        receipt.setClearingFileName(clearingFile.getFileName());
        receipt.setClearingFilePath(clearingFile.getRelativePath());
    }

    private List<NotificationOrder> createNotificationOrders(
            List<LineProcessingResult> results,
            Map<UUID, BatchLineObservation> observationByLineId,
            OffsetDateTime now) {
        List<NotificationOrder> orders = new ArrayList<>();
        for (LineProcessingResult result : results) {
            if (!Boolean.TRUE.equals(result.getBillable())) {
                continue;
            }
            if (notificationOrderRepository.findByLineId(result.getLineId()).isPresent()) {
                continue;
            }
            BatchLineObservation observation = observationByLineId.get(result.getLineId());
            String email = chooseNotificationEmail(result, observation);
            NotificationOrder order = new NotificationOrder();
            order.setNotificationOrderId(UUID.randomUUID());
            order.setBatchId(result.getBatchId());
            order.setLineId(result.getLineId());
            order.setNotificationEmail(email);
            order.setBeneficiaryName(observation == null ? null : observation.getBeneficiaryName());
            order.setAmount(result.getAmount());
            order.setCurrency(result.getCurrency());
            order.setFinalStatus(result.getFinalStatus());
            order.setMessage(resolveNotificationMessage(result));
            if (email == null || email.isBlank()) {
                order.setStatus(NotificationOrderStatus.OMITIDA.name());
            } else {
                applyNotificationServiceResult(order, result);
            }
            order.setCreatedAt(now);
            orders.add(order);
        }
        return orders;
    }

    private void applyNotificationServiceResult(NotificationOrder order, LineProcessingResult result) {
        NotificationRequest request = buildNotificationRequest(order, result);
        NotificationClientResult notificationResult = notificationClient.requestNotification(request);
        if (Boolean.TRUE.equals(notificationResult.getSuccessful())) {
            order.setStatus(NotificationOrderStatus.GENERADA.name());
            order.setMessage(appendNotificationResult(order.getMessage(), notificationResult));
            return;
        }
        order.setStatus(NotificationOrderStatus.FALLIDA.name());
        order.setMessage(appendNotificationResult(order.getMessage(), notificationResult));
    }

    private NotificationRequest buildNotificationRequest(NotificationOrder order, LineProcessingResult result) {
        NotificationRequest request = new NotificationRequest();
        request.setCorrelationId(toString(result.getCorrelationId()));
        request.setEventType(resolveNotificationEventType(result.getFinalStatus()));
        request.setOriginService(notificationOriginService);
        request.setPriority(notificationPriority);
        request.setChannelType(notificationChannelType);
        request.setRecipient(order.getNotificationEmail());
        request.setRecipientName(order.getBeneficiaryName());
        request.setTemplateCode(null);
        request.setSubject(notificationSubject);
        request.setBody(order.getMessage());
        request.setPayload(buildNotificationPayload(result));
        request.setEvidenceDocumentUuid(null);
        return request;
    }

    private Map<String, String> buildNotificationPayload(LineProcessingResult result) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("batchId", toString(result.getBatchId()));
        payload.put("lineId", toString(result.getLineId()));
        payload.put("amount", result.getAmount() == null ? null : result.getAmount().toPlainString());
        payload.put("currency", result.getCurrency());
        payload.put("finalStatus", result.getFinalStatus());
        return payload;
    }

    private String resolveNotificationEventType(String finalStatus) {
        if (STATUS_ACREDITADA_ON_US.equals(finalStatus)) {
            return notificationOnUsEventType;
        }
        if (STATUS_INCLUIDA_ARCHIVO_COMPENSACION.equals(finalStatus)) {
            return notificationOffUsEventType;
        }
        return "MASS_PAYMENT_PROCESSED";
    }

    private String appendNotificationResult(String currentMessage, NotificationClientResult notificationResult) {
        String baseMessage = currentMessage == null ? "" : currentMessage;
        String notificationMessage = notificationResult.getMessage();
        if (notificationResult.getNotificationUuid() != null) {
            notificationMessage = notificationMessage + ". notificationUuid=" + notificationResult.getNotificationUuid();
        }
        if (notificationResult.getStatus() != null && !notificationResult.getStatus().isBlank()) {
            notificationMessage = notificationMessage + ". notificationStatus=" + notificationResult.getStatus();
        }
        return limitText(baseMessage + " " + notificationMessage, 500);
    }

    private void registerGeneratedDocument(
            UUID batchId,
            String documentType,
            UUID documentId,
            String fileName,
            String filePath,
            OffsetDateTime now) {
        if (generatedDocumentRepository.findByBatchIdAndDocumentType(batchId, documentType).isPresent()) {
            return;
        }
        GeneratedDocument document = new GeneratedDocument();
        document.setGeneratedDocumentId(UUID.randomUUID());
        document.setBatchId(batchId);
        document.setDocumentType(documentType);
        document.setDocumentId(documentId);
        document.setFileName(fileName);
        document.setFilePath(filePath);
        document.setStatus(FinalReportingStatus.GENERADO.name());
        document.setGeneratedAt(now);
        document.setCreatedAt(now);
        generatedDocumentRepository.save(document);
    }

    private Map<UUID, BatchLineObservation> indexObservations(List<BatchLineObservation> observations) {
        Map<UUID, BatchLineObservation> indexed = new HashMap<>();
        for (BatchLineObservation observation : observations) {
            indexed.put(observation.getLineId(), observation);
        }
        return indexed;
    }

    private Integer sequenceOf(LineProcessingResult result, Map<UUID, BatchLineObservation> observationByLineId) {
        BatchLineObservation observation = observationByLineId.get(result.getLineId());
        return observation == null ? null : observation.getSequenceNumber();
    }

    private String resolveRouteType(LineProcessingResult result, BatchLineObservation observation) {
        if (STATUS_ACREDITADA_ON_US.equals(result.getFinalStatus())) {
            return "ON_US";
        }
        if (STATUS_INCLUIDA_ARCHIVO_COMPENSACION.equals(result.getFinalStatus())) {
            return "OFF_US";
        }
        if (observation != null && "10".equals(observation.getRoutingCode())) {
            return "ON_US";
        }
        if (observation != null && observation.getRoutingCode() != null && !observation.getRoutingCode().isBlank()) {
            return "OFF_US";
        }
        return "NO_DETERMINADA";
    }

    private String resolveMessage(LineProcessingResult result) {
        if (STATUS_ACREDITADA_ON_US.equals(result.getFinalStatus())) {
            return "Pago acreditado en cuenta BanQuito.";
        }
        if (STATUS_INCLUIDA_ARCHIVO_COMPENSACION.equals(result.getFinalStatus())) {
            return "Pago incluido en archivo de compensacion.";
        }
        if (STATUS_RECHAZADA.equals(result.getFinalStatus())) {
            return result.getRejectionReason();
        }
        if (STATUS_FALLIDA.equals(result.getFinalStatus())) {
            return result.getRejectionReason() == null ? "Falla tecnica controlada durante el procesamiento." : result.getRejectionReason();
        }
        return "Resultado final registrado por el Switch.";
    }

    private String resolveNotificationMessage(LineProcessingResult result) {
        if (STATUS_ACREDITADA_ON_US.equals(result.getFinalStatus())) {
            return ON_US_NOTIFICATION_MESSAGE;
        }
        if (STATUS_INCLUIDA_ARCHIVO_COMPENSACION.equals(result.getFinalStatus())) {
            return OFF_US_NOTIFICATION_MESSAGE;
        }
        return resolveMessage(result);
    }

    private String chooseNotificationEmail(LineProcessingResult result, BatchLineObservation observation) {
        if (result.getNotificationEmail() != null && !result.getNotificationEmail().isBlank()) {
            return result.getNotificationEmail();
        }
        return observation == null ? null : observation.getNotificationEmail();
    }

    private void validateEvent(BillingCompletedEvent event) {
        if (event == null || event.getEventId() == null || event.getBatchId() == null || event.getCorrelationId() == null) {
            throw new IllegalArgumentException("BillingCompletedEvent debe incluir eventId, batchId y correlationId");
        }
    }

    private Integer defaultInteger(Integer value) {
        return value == null ? 0 : value;
    }

    private BigDecimal defaultAmount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String requireCurrency(String primaryCurrency, String fallbackCurrency) {
        if (primaryCurrency != null && !primaryCurrency.isBlank()) {
            return primaryCurrency;
        }
        if (fallbackCurrency != null && !fallbackCurrency.isBlank()) {
            return fallbackCurrency;
        }
        return "USD";
    }

    private String toString(UUID value) {
        return value == null ? null : value.toString();
    }

    private String limitText(String value, Integer maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
