package com.banquito.switchpagos.reporting.service.impl;

import com.banquito.switchpagos.reporting.dto.response.BatchCommissionResponse;
import com.banquito.switchpagos.reporting.dto.response.BatchSummaryResponse;
import com.banquito.switchpagos.reporting.dto.response.CorporateReceiptResponse;
import com.banquito.switchpagos.reporting.dto.response.GeneratedFileResponse;
import com.banquito.switchpagos.reporting.dto.response.NoveltyDetailResponse;
import com.banquito.switchpagos.reporting.dto.response.NoveltyDetailsResponse;
import com.banquito.switchpagos.reporting.dto.response.NotificationOrderResponse;
import com.banquito.switchpagos.reporting.dto.response.NotificationOrdersResponse;
import com.banquito.switchpagos.reporting.exception.ResourceNotFoundException;
import com.banquito.switchpagos.reporting.model.BatchProcessingSummary;
import com.banquito.switchpagos.reporting.model.ClearingFile;
import com.banquito.switchpagos.reporting.model.CorporateReceipt;
import com.banquito.switchpagos.reporting.model.NotificationOrder;
import com.banquito.switchpagos.reporting.model.NoveltyReport;
import com.banquito.switchpagos.reporting.model.NoveltyReportLine;
import com.banquito.switchpagos.reporting.repository.BatchProcessingSummaryRepository;
import com.banquito.switchpagos.reporting.repository.ClearingFileRepository;
import com.banquito.switchpagos.reporting.repository.CorporateReceiptRepository;
import com.banquito.switchpagos.reporting.repository.NotificationOrderRepository;
import com.banquito.switchpagos.reporting.repository.NoveltyReportLineRepository;
import com.banquito.switchpagos.reporting.repository.NoveltyReportRepository;
import com.banquito.switchpagos.reporting.service.ReportingQueryService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportingQueryServiceImpl implements ReportingQueryService {

    private static final String UNPROCESSED_AMOUNT_POLICY =
            "El monto no procesado no se libera ni se devuelve a la empresa.";

    private final BatchProcessingSummaryRepository summaryRepository;
    private final NoveltyReportRepository noveltyReportRepository;
    private final NoveltyReportLineRepository noveltyReportLineRepository;
    private final CorporateReceiptRepository corporateReceiptRepository;
    private final ClearingFileRepository clearingFileRepository;
    private final NotificationOrderRepository notificationOrderRepository;
    private final Path outputDirectory;

    public ReportingQueryServiceImpl(
            BatchProcessingSummaryRepository summaryRepository,
            NoveltyReportRepository noveltyReportRepository,
            NoveltyReportLineRepository noveltyReportLineRepository,
            CorporateReceiptRepository corporateReceiptRepository,
            ClearingFileRepository clearingFileRepository,
            NotificationOrderRepository notificationOrderRepository,
            @Value("${switch.files.output-directory}") String outputDirectory) {
        this.summaryRepository = summaryRepository;
        this.noveltyReportRepository = noveltyReportRepository;
        this.noveltyReportLineRepository = noveltyReportLineRepository;
        this.corporateReceiptRepository = corporateReceiptRepository;
        this.clearingFileRepository = clearingFileRepository;
        this.notificationOrderRepository = notificationOrderRepository;
        this.outputDirectory = Path.of(outputDirectory).normalize();
    }

    @Override
    @Transactional(readOnly = true)
    public BatchSummaryResponse getBatchSummary(UUID batchId) {
        BatchProcessingSummary summary = summaryRepository.findByBatchId(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe resumen de procesamiento para el lote " + batchId));
        BatchSummaryResponse response = new BatchSummaryResponse();
        response.setBatchId(summary.getBatchId());
        response.setCorrelationId(summary.getCorrelationId());
        response.setCompanyRuc(summary.getCompanyRuc());
        response.setSourceAccountNumber(summary.getSourceAccountNumber());
        response.setCoreFundingId(summary.getCoreFundingId());
        response.setTotalLines(resolveTotalLines(summary));
        response.setExpectedTotalLines(summary.getExpectedTotalLines());
        response.setObservedLines(summary.getObservedLines());
        response.setFinalResultLines(summary.getFinalResultLines());
        response.setOnUsCreditedLines(summary.getOnUsCreditedLines());
        response.setOffUsIncludedLines(summary.getOffUsIncludedLines());
        response.setRejectedLines(summary.getRejectedLines());
        response.setFailedLines(summary.getFailedLines());
        response.setBillableLines(summary.getBillableLines());
        response.setControlAmount(summary.getControlAmount());
        response.setProcessedAmount(summary.getProcessedAmount());
        response.setRemainingAmount(summary.getRemainingAmount());
        response.setUnprocessedAmount(summary.getRemainingAmount());
        response.setUnprocessedAmountReturned(Boolean.FALSE);
        response.setUnprocessedAmountPolicy(UNPROCESSED_AMOUNT_POLICY);
        response.setCurrency(summary.getCurrency());
        response.setStatus(summary.getStatus());
        response.setCompletedAt(summary.getCompletedAt());
        response.setUpdatedAt(summary.getUpdatedAt());
        corporateReceiptRepository.findByBatchId(batchId).ifPresent(receipt -> {
            response.setBillingStatus(receipt.getBillingStatus());
            response.setCommissionSubtotal(receipt.getCommissionSubtotal());
            response.setTotalChargedAmount(receipt.getTotalChargedAmount());
            response.setGeneratedAt(receipt.getGeneratedAt());
        });
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public GeneratedFileResponse getNoveltyReport(UUID batchId) {
        NoveltyReport report = noveltyReportRepository.findByBatchId(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe reporte de novedades para el lote " + batchId));
        GeneratedFileResponse response = new GeneratedFileResponse();
        response.setBatchId(report.getBatchId());
        response.setDocumentId(report.getNoveltyReportId());
        response.setStatus(report.getStatus());
        response.setFileName(report.getFileName());
        response.setFilePath(report.getFilePath());
        response.setContentType("text/csv");
        response.setContent(readGeneratedFile(report.getFilePath()));
        response.setGeneratedAt(report.getGeneratedAt());
        response.setTotalLines(report.getTotalLines());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public NoveltyDetailsResponse getNoveltyDetails(UUID batchId) {
        if (summaryRepository.findByBatchId(batchId).isEmpty() && noveltyReportRepository.findByBatchId(batchId).isEmpty()) {
            throw new ResourceNotFoundException("No existen novedades para el lote " + batchId);
        }
        List<NoveltyDetailResponse> details = noveltyReportLineRepository.findByBatchIdOrderBySequenceNumberAsc(batchId)
                .stream()
                .map(this::toNoveltyDetailResponse)
                .toList();
        NoveltyDetailsResponse response = new NoveltyDetailsResponse();
        response.setBatchId(batchId);
        response.setTotalNovelties(details.size());
        response.setNovelties(details);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CorporateReceiptResponse getCorporateReceipt(UUID batchId) {
        CorporateReceipt receipt = corporateReceiptRepository.findByBatchId(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe comprobante corporativo para el lote " + batchId));
        CorporateReceiptResponse response = new CorporateReceiptResponse();
        response.setBatchId(receipt.getBatchId());
        response.setReceiptId(receipt.getCorporateReceiptId());
        response.setFileName(receipt.getFileName());
        response.setFilePath(receipt.getFilePath());
        response.setContent(readGeneratedFile(receipt.getFilePath()));
        response.setGeneratedAt(receipt.getGeneratedAt());
        response.setCompanyRuc(receipt.getCompanyRuc());
        response.setSourceAccountNumber(receipt.getSourceAccountNumber());
        response.setTotalReceivedLines(receipt.getTotalReceivedLines());
        response.setSuccessfulOnUsLines(receipt.getSuccessfulOnUsLines());
        response.setIncludedOffUsLines(receipt.getIncludedOffUsLines());
        response.setRejectedLines(receipt.getRejectedLines());
        response.setFailedLines(receipt.getFailedLines());
        response.setBillableLines(receipt.getBillableLines());
        response.setTotalControlAmount(receipt.getTotalControlAmount());
        response.setFundedAmount(receipt.getTotalControlAmount());
        response.setTotalProcessedAmount(receipt.getTotalProcessedAmount());
        response.setRemainingAmount(receipt.getRemainingAmount());
        response.setUnprocessedAmount(receipt.getRemainingAmount());
        response.setUnprocessedAmountReturned(Boolean.FALSE);
        response.setUnprocessedAmountPolicy(UNPROCESSED_AMOUNT_POLICY);
        response.setReleasedAmount(null);
        response.setUnitFee(receipt.getUnitFee());
        response.setCommissionSubtotal(receipt.getCommissionSubtotal());
        response.setTaxAmount(receipt.getTaxAmount());
        response.setTotalChargedAmount(receipt.getTotalChargedAmount());
        response.setCurrency(receipt.getCurrency());
        response.setBillingStatus(receipt.getBillingStatus());
        response.setCoreCommissionChargeId(receipt.getCoreCommissionChargeId());
        summaryRepository.findByBatchId(batchId).ifPresent(summary -> {
            response.setCoreFundingId(summary.getCoreFundingId());
            response.setReservationUuid(summary.getCoreFundingId());
            if (summary.getControlAmount() != null) {
                response.setFundedAmount(summary.getControlAmount());
            }
        });
        clearingFileRepository.findByBatchId(batchId).ifPresent(clearingFile -> {
            response.setClearingFileName(clearingFile.getFileName());
            response.setClearingFilePath(clearingFile.getRelativePath());
        });
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BatchCommissionResponse getCommission(UUID batchId) {
        CorporateReceipt receipt = corporateReceiptRepository.findByBatchId(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe detalle de comision para el lote " + batchId));
        BatchCommissionResponse response = new BatchCommissionResponse();
        response.setBatchId(receipt.getBatchId());
        response.setBillableLines(receipt.getBillableLines());
        response.setUnitFee(receipt.getUnitFee());
        response.setCommissionSubtotal(receipt.getCommissionSubtotal());
        response.setTaxAmount(receipt.getTaxAmount());
        response.setTotalCommission(receipt.getTotalChargedAmount() != null
                ? receipt.getTotalChargedAmount()
                : receipt.getCommissionSubtotal());
        response.setBillingStatus(receipt.getBillingStatus());
        response.setCoreStatus(null);
        response.setExternalReference("COMMISSION-" + receipt.getBatchId());
        response.setChargedAt(receipt.getGeneratedAt());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public GeneratedFileResponse getClearingFile(UUID batchId) {
        ClearingFile clearingFile = clearingFileRepository.findByBatchId(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe archivo de compensacion para el lote " + batchId));
        GeneratedFileResponse response = new GeneratedFileResponse();
        response.setBatchId(clearingFile.getBatchId());
        response.setDocumentId(clearingFile.getClearingFileId());
        response.setStatus(clearingFile.getStatus());
        response.setFileName(clearingFile.getFileName());
        response.setFilePath(clearingFile.getRelativePath());
        response.setContentType("text/csv");
        response.setContent(readGeneratedFile(clearingFile.getRelativePath()));
        response.setGeneratedAt(clearingFile.getUpdatedAt());
        response.setTotalLines(clearingFile.getTotalLines());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationOrdersResponse getNotificationOrders(UUID batchId) {
        if (summaryRepository.findByBatchId(batchId).isEmpty()) {
            throw new ResourceNotFoundException("No existe resumen de procesamiento para el lote " + batchId);
        }
        List<NotificationOrder> orders = notificationOrderRepository.findByBatchId(batchId);
        List<NotificationOrderResponse> notificationResponses = orders.stream()
                .sorted(Comparator
                        .comparing(NotificationOrder::getCreatedAt, Comparator.nullsLast(OffsetDateTime::compareTo))
                        .thenComparing(NotificationOrder::getLineId, Comparator.nullsLast(UUID::compareTo)))
                .map(this::toNotificationOrderResponse)
                .toList();
        NotificationOrdersResponse response = new NotificationOrdersResponse();
        response.setBatchId(batchId);
        response.setTotalOrders(notificationResponses.size());
        response.setNotifications(notificationResponses);
        return response;
    }

    private NotificationOrderResponse toNotificationOrderResponse(NotificationOrder order) {
        NotificationOrderResponse response = new NotificationOrderResponse();
        response.setNotificationOrderId(order.getNotificationOrderId());
        response.setBatchId(order.getBatchId());
        response.setLineId(order.getLineId());
        response.setNotificationEmail(order.getNotificationEmail());
        response.setBeneficiaryName(order.getBeneficiaryName());
        response.setAmount(order.getAmount());
        response.setCurrency(order.getCurrency());
        response.setFinalStatus(order.getFinalStatus());
        response.setStatus(order.getStatus());
        response.setMessage(order.getMessage());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }

    private NoveltyDetailResponse toNoveltyDetailResponse(NoveltyReportLine line) {
        NoveltyDetailResponse response = new NoveltyDetailResponse();
        response.setLineId(line.getLineId());
        response.setSequenceNumber(line.getSequenceNumber());
        response.setBeneficiaryIdentification(line.getBeneficiaryIdentification());
        response.setBeneficiaryName(line.getBeneficiaryName());
        response.setDestinationAccountNumber(line.getDestinationAccountNumber());
        response.setAmount(line.getAmount());
        response.setCurrency(line.getCurrency());
        response.setFinalStatus(line.getFinalStatus());
        response.setNoveltyType(resolveNoveltyType(line.getFinalStatus()));
        response.setErrorCode(line.getRejectionCode());
        response.setErrorMessage(resolveNoveltyMessage(line));
        response.setErrorSource(resolveErrorSource(line.getRouteType(), line.getFinalStatus()));
        response.setProcessedAt(line.getCreatedAt());
        return response;
    }

    private String resolveNoveltyType(String finalStatus) {
        if ("RECHAZADA".equals(finalStatus)) {
            return "RECHAZO";
        }
        if ("FALLIDA".equals(finalStatus)) {
            return "FALLA";
        }
        return "INFORMATIVA";
    }

    private String resolveNoveltyMessage(NoveltyReportLine line) {
        if (line.getRejectionReason() != null && !line.getRejectionReason().isBlank()) {
            return line.getRejectionReason();
        }
        return line.getMessage();
    }

    private String resolveErrorSource(String routeType, String finalStatus) {
        if ("RECHAZADA".equals(finalStatus) || "FALLIDA".equals(finalStatus)) {
            if (routeType != null && !routeType.isBlank()) {
                return routeType;
            }
            return "SWITCH";
        }
        return null;
    }

    private Integer resolveTotalLines(BatchProcessingSummary summary) {
        if (summary.getExpectedTotalLines() != null) {
            return summary.getExpectedTotalLines();
        }
        return summary.getObservedLines();
    }

    private String readGeneratedFile(String relativePath) {
        Path resolvedFile = outputDirectory.resolve(relativePath).normalize();
        if (!resolvedFile.startsWith(outputDirectory)) {
            throw new IllegalArgumentException("Ruta de archivo generado no permitida.");
        }
        if (!Files.exists(resolvedFile)) {
            throw new ResourceNotFoundException("El archivo generado no existe en almacenamiento local.");
        }
        try {
            return Files.readString(resolvedFile, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible leer el archivo generado.", exception);
        }
    }
}
