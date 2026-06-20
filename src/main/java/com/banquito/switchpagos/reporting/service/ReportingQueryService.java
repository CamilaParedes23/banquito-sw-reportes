package com.banquito.switchpagos.reporting.service;

import com.banquito.switchpagos.reporting.dto.response.BatchCommissionResponse;
import com.banquito.switchpagos.reporting.dto.response.BatchSummaryResponse;
import com.banquito.switchpagos.reporting.dto.response.CorporateReceiptResponse;
import com.banquito.switchpagos.reporting.dto.response.GeneratedFileResponse;
import com.banquito.switchpagos.reporting.dto.response.NoveltyDetailsResponse;
import com.banquito.switchpagos.reporting.dto.response.NotificationOrdersResponse;
import java.util.UUID;

public interface ReportingQueryService {

    BatchSummaryResponse getBatchSummary(UUID batchId);

    GeneratedFileResponse getNoveltyReport(UUID batchId);

    NoveltyDetailsResponse getNoveltyDetails(UUID batchId);

    CorporateReceiptResponse getCorporateReceipt(UUID batchId);

    BatchCommissionResponse getCommission(UUID batchId);

    GeneratedFileResponse getClearingFile(UUID batchId);

    NotificationOrdersResponse getNotificationOrders(UUID batchId);
}
