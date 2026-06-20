package com.banquito.switchpagos.reporting.controller;

import com.banquito.switchpagos.reporting.dto.response.BatchCommissionResponse;
import com.banquito.switchpagos.reporting.dto.response.BatchSummaryResponse;
import com.banquito.switchpagos.reporting.dto.response.CorporateReceiptResponse;
import com.banquito.switchpagos.reporting.dto.response.GeneratedFileResponse;
import com.banquito.switchpagos.reporting.dto.response.NoveltyDetailsResponse;
import com.banquito.switchpagos.reporting.dto.response.NotificationOrdersResponse;
import com.banquito.switchpagos.reporting.service.ReportingQueryService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/batches")
public class ReportingRestController {

    private final ReportingQueryService reportingQueryService;

    public ReportingRestController(ReportingQueryService reportingQueryService) {
        this.reportingQueryService = reportingQueryService;
    }

    @GetMapping("/{batchId}/summary")
    public BatchSummaryResponse getBatchSummary(@PathVariable UUID batchId) {
        return reportingQueryService.getBatchSummary(batchId);
    }

    @GetMapping("/{batchId}/reports/novelties")
    public GeneratedFileResponse getNoveltyReport(@PathVariable UUID batchId) {
        return reportingQueryService.getNoveltyReport(batchId);
    }

    @GetMapping("/{batchId}/novelties/details")
    public NoveltyDetailsResponse getNoveltyDetails(@PathVariable UUID batchId) {
        return reportingQueryService.getNoveltyDetails(batchId);
    }

    @GetMapping("/{batchId}/receipts/corporate")
    public CorporateReceiptResponse getCorporateReceipt(@PathVariable UUID batchId) {
        return reportingQueryService.getCorporateReceipt(batchId);
    }

    @GetMapping("/{batchId}/commission")
    public BatchCommissionResponse getCommission(@PathVariable UUID batchId) {
        return reportingQueryService.getCommission(batchId);
    }

    @GetMapping("/{batchId}/clearing-file")
    public GeneratedFileResponse getClearingFile(@PathVariable UUID batchId) {
        return reportingQueryService.getClearingFile(batchId);
    }

    @GetMapping("/{batchId}/notifications")
    public NotificationOrdersResponse getNotificationOrders(@PathVariable UUID batchId) {
        return reportingQueryService.getNotificationOrders(batchId);
    }
}
