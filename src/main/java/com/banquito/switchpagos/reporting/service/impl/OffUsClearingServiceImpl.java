package com.banquito.switchpagos.reporting.service.impl;

import com.banquito.switchpagos.reporting.dto.event.OffUsClearingIncludedEvent;
import com.banquito.switchpagos.reporting.dto.event.PaymentLineRoutedOffUsEvent;
import com.banquito.switchpagos.reporting.enums.ClearingLineStatus;
import com.banquito.switchpagos.reporting.exception.ClearingFileGenerationException;
import com.banquito.switchpagos.reporting.file.ClearingFileWriter;
import com.banquito.switchpagos.reporting.mapper.OffUsClearingMapper;
import com.banquito.switchpagos.reporting.model.ClearingFile;
import com.banquito.switchpagos.reporting.model.ClearingFileLine;
import com.banquito.switchpagos.reporting.repository.ClearingFileLineRepository;
import com.banquito.switchpagos.reporting.repository.ClearingFileRepository;
import com.banquito.switchpagos.reporting.service.LineConsolidationService;
import com.banquito.switchpagos.reporting.service.OffUsClearingEventPublisher;
import com.banquito.switchpagos.reporting.service.OffUsClearingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OffUsClearingServiceImpl implements OffUsClearingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OffUsClearingServiceImpl.class);

    private final ClearingFileRepository clearingFileRepository;
    private final ClearingFileLineRepository clearingFileLineRepository;
    private final OffUsClearingMapper clearingMapper;
    private final ClearingFileWriter clearingFileWriter;
    private final OffUsClearingEventPublisher eventPublisher;
    private final LineConsolidationService lineConsolidationService;

    public OffUsClearingServiceImpl(
            ClearingFileRepository clearingFileRepository,
            ClearingFileLineRepository clearingFileLineRepository,
            OffUsClearingMapper clearingMapper,
            ClearingFileWriter clearingFileWriter,
            OffUsClearingEventPublisher eventPublisher,
            LineConsolidationService lineConsolidationService) {
        this.clearingFileRepository = clearingFileRepository;
        this.clearingFileLineRepository = clearingFileLineRepository;
        this.clearingMapper = clearingMapper;
        this.clearingFileWriter = clearingFileWriter;
        this.eventPublisher = eventPublisher;
        this.lineConsolidationService = lineConsolidationService;
    }

    @Override
    @Transactional
    public synchronized void includeOffUsLine(PaymentLineRoutedOffUsEvent event) {
        validateEvent(event);
        ClearingFileLine existingLine = clearingFileLineRepository.findByLineId(event.getLineId()).orElse(null);
        if (existingLine != null && ClearingLineStatus.INCLUIDA_ARCHIVO_COMPENSACION.name().equals(existingLine.getStatus())) {
            LOGGER.info("Linea Off-Us ya incluida en archivo. batchId={}, lineId={}, status={}",
                    existingLine.getBatchId(), existingLine.getLineId(), existingLine.getStatus());
            return;
        }
        if (existingLine != null && ClearingLineStatus.FALLIDA.name().equals(existingLine.getStatus())) {
            LOGGER.info("Linea Off-Us ya fue marcada fallida. batchId={}, lineId={}, status={}",
                    existingLine.getBatchId(), existingLine.getLineId(), existingLine.getStatus());
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        ClearingFile clearingFile = getOrCreateClearingFile(event, now);
        ClearingFileLine line = existingLine;
        if (line == null) {
            line = clearingMapper.toLine(event, clearingFile, now);
            clearingFileLineRepository.save(line);
        }

        List<ClearingFileLine> includedLines = clearingFileLineRepository.findByBatchIdAndStatusOrderBySequenceNumberAsc(
                event.getBatchId(), ClearingLineStatus.INCLUIDA_ARCHIVO_COMPENSACION.name());
        try {
            clearingFileWriter.writeClearingFile(clearingFile, includedLines, line);
        } catch (ClearingFileGenerationException exception) {
            OffsetDateTime failureTime = OffsetDateTime.now();
            clearingMapper.markFailed(line, "CLEARING_FILE_GENERATION_ERROR", limitMessage(exception.getMessage()), failureTime);
            clearingMapper.markFileFailed(clearingFile, failureTime);
            clearingFileLineRepository.save(line);
            clearingFileRepository.save(clearingFile);
            lineConsolidationService.registerOffUsFailure(line);
            LOGGER.error("Error generando archivo de compensacion. batchId={}, lineId={}, reason={}",
                    event.getBatchId(), event.getLineId(), exception.getMessage());
            return;
        }

        OffsetDateTime includedAt = OffsetDateTime.now();
        clearingMapper.markIncluded(line, includedAt);
        BigDecimal totalAmount = calculateTotalAmount(includedLines).add(line.getAmount());
        clearingMapper.updateFileTotals(clearingFile, includedLines.size() + 1, totalAmount, includedAt);
        clearingFileLineRepository.save(line);
        clearingFileRepository.save(clearingFile);

        OffUsClearingIncludedEvent includedEvent = clearingMapper.toIncludedEvent(clearingFile, line, UUID.randomUUID(), OffsetDateTime.now());
        eventPublisher.publishIncluded(includedEvent);
        lineConsolidationService.registerOffUsIncluded(includedEvent);
        LOGGER.info("Linea Off-Us incluida y evento publicado. batchId={}, lineId={}, fileName={}, billable={}",
                line.getBatchId(), line.getLineId(), clearingFile.getFileName(), line.getBillable());
    }

    private ClearingFile getOrCreateClearingFile(PaymentLineRoutedOffUsEvent event, OffsetDateTime now) {
        return clearingFileRepository.findByBatchId(event.getBatchId())
                .orElseGet(() -> clearingFileRepository.save(clearingMapper.toNewClearingFile(event, now)));
    }

    private BigDecimal calculateTotalAmount(List<ClearingFileLine> includedLines) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ClearingFileLine includedLine : includedLines) {
            totalAmount = totalAmount.add(includedLine.getAmount());
        }
        return totalAmount;
    }

    private void validateEvent(PaymentLineRoutedOffUsEvent event) {
        if (event == null || event.getEventId() == null || event.getBatchId() == null
                || event.getLineId() == null || event.getCorrelationId() == null) {
            throw new IllegalArgumentException("PaymentLineRoutedOffUsEvent debe incluir eventId, batchId, lineId y correlationId");
        }
        if (event.getBeneficiaryIdentification() == null || event.getBeneficiaryIdentification().isBlank()
                || event.getBeneficiaryName() == null || event.getBeneficiaryName().isBlank()
                || event.getDestinationAccountNumber() == null || event.getDestinationAccountNumber().isBlank()) {
            throw new IllegalArgumentException("PaymentLineRoutedOffUsEvent debe incluir datos minimos del beneficiario y cuenta destino");
        }
        if (event.getRoutingCode() == null || event.getRoutingCode().isBlank()) {
            throw new IllegalArgumentException("PaymentLineRoutedOffUsEvent debe incluir routingCode");
        }
        if (event.getAmount() == null || event.getCurrency() == null || event.getCurrency().isBlank()) {
            throw new IllegalArgumentException("PaymentLineRoutedOffUsEvent debe incluir amount y currency");
        }
    }

    private String limitMessage(String message) {
        if (message == null) {
            return "Error tecnico al generar archivo de compensacion";
        }
        if (message.length() <= 500) {
            return message;
        }
        return message.substring(0, 500);
    }
}
