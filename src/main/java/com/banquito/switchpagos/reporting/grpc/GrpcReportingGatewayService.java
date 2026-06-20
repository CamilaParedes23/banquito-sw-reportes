package com.banquito.switchpagos.reporting.grpc;

import com.banquito.switchpagos.reporting.dto.response.BatchSummaryResponse;
import com.banquito.switchpagos.reporting.dto.response.CorporateReceiptResponse;
import com.banquito.switchpagos.reporting.dto.response.GeneratedFileResponse;
import com.banquito.switchpagos.reporting.exception.ResourceNotFoundException;
import com.banquito.switchpagos.reporting.service.ReportingQueryService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GrpcReportingGatewayService extends ReportingGatewayServiceGrpc.ReportingGatewayServiceImplBase {

    private final ReportingQueryService reportingQueryService;

    public GrpcReportingGatewayService(ReportingQueryService reportingQueryService) {
        this.reportingQueryService = reportingQueryService;
    }

    @Override
    public void getBatchSummary(
            ReportingBatchIdGrpcRequest request,
            StreamObserver<BatchSummaryGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(reportingQueryService.getBatchSummary(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    @Override
    public void getNoveltyReport(
            ReportingBatchIdGrpcRequest request,
            StreamObserver<GeneratedFileGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(reportingQueryService.getNoveltyReport(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    @Override
    public void getCorporateReceipt(
            ReportingBatchIdGrpcRequest request,
            StreamObserver<CorporateReceiptGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(reportingQueryService.getCorporateReceipt(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    @Override
    public void getClearingFile(
            ReportingBatchIdGrpcRequest request,
            StreamObserver<GeneratedFileGrpcResponse> responseObserver) {
        try {
            responseObserver.onNext(toGrpc(reportingQueryService.getClearingFile(parseBatchId(request.getBatchId()))));
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(toStatus(exception));
        }
    }

    private UUID parseBatchId(String batchId) {
        try {
            return UUID.fromString(batchId);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("El batchId no tiene formato UUID valido.", exception);
        }
    }

    private BatchSummaryGrpcResponse toGrpc(BatchSummaryResponse response) {
        return BatchSummaryGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()))
                .setCorrelationId(toString(response.getCorrelationId()))
                .setCompanyRuc(nullToEmpty(response.getCompanyRuc()))
                .setSourceAccountNumber(nullToEmpty(response.getSourceAccountNumber()))
                .setCoreFundingId(nullToEmpty(response.getCoreFundingId()))
                .setExpectedTotalLines(toInt(response.getExpectedTotalLines()))
                .setObservedLines(toInt(response.getObservedLines()))
                .setFinalResultLines(toInt(response.getFinalResultLines()))
                .setOnUsCreditedLines(toInt(response.getOnUsCreditedLines()))
                .setOffUsIncludedLines(toInt(response.getOffUsIncludedLines()))
                .setRejectedLines(toInt(response.getRejectedLines()))
                .setFailedLines(toInt(response.getFailedLines()))
                .setBillableLines(toInt(response.getBillableLines()))
                .setControlAmount(toString(response.getControlAmount()))
                .setProcessedAmount(toString(response.getProcessedAmount()))
                .setRemainingAmount(toString(response.getRemainingAmount()))
                .setCurrency(nullToEmpty(response.getCurrency()))
                .setStatus(nullToEmpty(response.getStatus()))
                .setCompletedAt(toString(response.getCompletedAt()))
                .setUpdatedAt(toString(response.getUpdatedAt()))
                .build();
    }

    private GeneratedFileGrpcResponse toGrpc(GeneratedFileResponse response) {
        return GeneratedFileGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()))
                .setDocumentId(toString(response.getDocumentId()))
                .setStatus(nullToEmpty(response.getStatus()))
                .setFileName(nullToEmpty(response.getFileName()))
                .setFilePath(nullToEmpty(response.getFilePath()))
                .setContentType(nullToEmpty(response.getContentType()))
                .setContent(nullToEmpty(response.getContent()))
                .setGeneratedAt(toString(response.getGeneratedAt()))
                .setTotalLines(toInt(response.getTotalLines()))
                .build();
    }

    private CorporateReceiptGrpcResponse toGrpc(CorporateReceiptResponse response) {
        return CorporateReceiptGrpcResponse.newBuilder()
                .setBatchId(toString(response.getBatchId()))
                .setReceiptId(toString(response.getReceiptId()))
                .setFileName(nullToEmpty(response.getFileName()))
                .setFilePath(nullToEmpty(response.getFilePath()))
                .setContent(nullToEmpty(response.getContent()))
                .setGeneratedAt(toString(response.getGeneratedAt()))
                .setCompanyRuc(nullToEmpty(response.getCompanyRuc()))
                .setSourceAccountNumber(nullToEmpty(response.getSourceAccountNumber()))
                .setTotalReceivedLines(toInt(response.getTotalReceivedLines()))
                .setSuccessfulOnUsLines(toInt(response.getSuccessfulOnUsLines()))
                .setIncludedOffUsLines(toInt(response.getIncludedOffUsLines()))
                .setRejectedLines(toInt(response.getRejectedLines()))
                .setFailedLines(toInt(response.getFailedLines()))
                .setBillableLines(toInt(response.getBillableLines()))
                .setTotalControlAmount(toString(response.getTotalControlAmount()))
                .setTotalProcessedAmount(toString(response.getTotalProcessedAmount()))
                .setRemainingAmount(toString(response.getRemainingAmount()))
                .setReleasedAmount("")
                .setUnitFee(toString(response.getUnitFee()))
                .setCommissionSubtotal(toString(response.getCommissionSubtotal()))
                .setTaxAmount(toString(response.getTaxAmount()))
                .setTotalChargedAmount(toString(response.getTotalChargedAmount()))
                .setCurrency(nullToEmpty(response.getCurrency()))
                .setBillingStatus(nullToEmpty(response.getBillingStatus()))
                .setCoreCommissionChargeId(nullToEmpty(response.getCoreCommissionChargeId()))
                .build();
    }

    private Throwable toStatus(Exception exception) {
        if (exception instanceof IllegalArgumentException) {
            return Status.INVALID_ARGUMENT.withDescription(exception.getMessage()).asRuntimeException();
        }
        if (exception instanceof ResourceNotFoundException) {
            return Status.NOT_FOUND.withDescription(exception.getMessage()).asRuntimeException();
        }
        return Status.INTERNAL.withDescription("Error tecnico en reporting-service.").asRuntimeException();
    }

    private String toString(UUID value) {
        return value == null ? "" : value.toString();
    }

    private String toString(OffsetDateTime value) {
        return value == null ? "" : value.toString();
    }

    private String toString(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private Integer toInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
