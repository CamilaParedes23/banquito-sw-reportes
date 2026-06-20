package com.banquito.switchpagos.reporting.file;

import com.banquito.switchpagos.reporting.model.CorporateReceipt;
import com.banquito.switchpagos.reporting.model.NoveltyReportLine;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FinalReportingFileWriter {

    private static final String NOVELTY_HEADER = "batchId,lineId,sequenceNumber,routeType,beneficiaryIdentification,beneficiaryName,destinationAccountNumber,routingCode,amount,currency,finalStatus,billable,rejectionCode,rejectionReason,message";
    private static final String UNPROCESSED_AMOUNT_POLICY =
            "El monto no procesado no se libera ni se devuelve a la empresa.";

    private final Path reportsDirectory;
    private final Path receiptsDirectory;

    public FinalReportingFileWriter(
            @Value("${switch.files.reports-directory}") String reportsDirectory,
            @Value("${switch.files.receipts-directory}") String receiptsDirectory) {
        this.reportsDirectory = Path.of(reportsDirectory).normalize();
        this.receiptsDirectory = Path.of(receiptsDirectory).normalize();
    }

    public void writeNoveltyReport(String fileName, List<NoveltyReportLine> reportLines) {
        List<NoveltyReportLine> sortedLines = new ArrayList<>(reportLines);
        sortedLines.sort(Comparator
                .comparing(NoveltyReportLine::getSequenceNumber, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(NoveltyReportLine::getLineId));
        try {
            Files.createDirectories(reportsDirectory);
            Path targetFile = reportsDirectory.resolve(fileName).normalize();
            Path temporaryFile = reportsDirectory.resolve(fileName + ".tmp").normalize();
            List<String> content = new ArrayList<>();
            content.add(NOVELTY_HEADER);
            for (NoveltyReportLine line : sortedLines) {
                content.add(toNoveltyCsvLine(line));
            }
            Files.write(temporaryFile, content, StandardCharsets.UTF_8);
            moveFile(temporaryFile, targetFile);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo generar el reporte de novedades", exception);
        }
    }

    public void writeCorporateReceipt(CorporateReceipt receipt) {
        try {
            Files.createDirectories(receiptsDirectory);
            Path targetFile = receiptsDirectory.resolve(receipt.getFileName()).normalize();
            Path temporaryFile = receiptsDirectory.resolve(receipt.getFileName() + ".tmp").normalize();
            Files.writeString(temporaryFile, toReceiptJson(receipt), StandardCharsets.UTF_8);
            moveFile(temporaryFile, targetFile);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo generar el comprobante corporativo", exception);
        }
    }

    private void moveFile(Path temporaryFile, Path targetFile) throws IOException {
        try {
            Files.move(temporaryFile, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String toNoveltyCsvLine(NoveltyReportLine line) {
        return csv(line.getBatchId()) + ","
                + csv(line.getLineId()) + ","
                + csv(line.getSequenceNumber()) + ","
                + csv(line.getRouteType()) + ","
                + csv(line.getBeneficiaryIdentification()) + ","
                + csv(line.getBeneficiaryName()) + ","
                + csv(line.getDestinationAccountNumber()) + ","
                + csv(line.getRoutingCode()) + ","
                + csv(toPlainAmount(line.getAmount())) + ","
                + csv(line.getCurrency()) + ","
                + csv(line.getFinalStatus()) + ","
                + csv(line.getBillable()) + ","
                + csv(line.getRejectionCode()) + ","
                + csv(line.getRejectionReason()) + ","
                + csv(line.getMessage());
    }

    private String toReceiptJson(CorporateReceipt receipt) {
        List<String> lines = new ArrayList<>();
        lines.add("{");
        addJsonLine(lines, "batchId", receipt.getBatchId(), true);
        addJsonLine(lines, "companyRuc", receipt.getCompanyRuc(), true);
        addJsonLine(lines, "sourceAccountNumber", receipt.getSourceAccountNumber(), true);
        addJsonLine(lines, "reservationUuid", receipt.getCoreFundingId(), true);
        addJsonLine(lines, "coreFundingId", receipt.getCoreFundingId(), true);
        addJsonLine(lines, "totalReceivedLines", receipt.getTotalReceivedLines(), false);
        addJsonLine(lines, "successfulOnUsLines", receipt.getSuccessfulOnUsLines(), false);
        addJsonLine(lines, "includedOffUsLines", receipt.getIncludedOffUsLines(), false);
        addJsonLine(lines, "rejectedLines", receipt.getRejectedLines(), false);
        addJsonLine(lines, "failedLines", receipt.getFailedLines(), false);
        addJsonLine(lines, "billableLines", receipt.getBillableLines(), false);
        addJsonLine(lines, "totalControlAmount", toPlainAmount(receipt.getTotalControlAmount()), true);
        addJsonLine(lines, "fundedAmount", toPlainAmount(receipt.getFundedAmount()), true);
        addJsonLine(lines, "totalProcessedAmount", toPlainAmount(receipt.getTotalProcessedAmount()), true);
        addJsonLine(lines, "remainingAmount", toPlainAmount(receipt.getRemainingAmount()), true);
        addJsonLine(lines, "unprocessedAmount", toPlainAmount(receipt.getRemainingAmount()), true);
        addJsonLine(lines, "unprocessedAmountReturned", Boolean.FALSE, false);
        addJsonLine(lines, "unprocessedAmountPolicy", UNPROCESSED_AMOUNT_POLICY, true);
        addJsonLine(lines, "unitFee", toPlainAmount(receipt.getUnitFee()), true);
        addJsonLine(lines, "commissionSubtotal", toPlainAmount(receipt.getCommissionSubtotal()), true);
        addJsonLine(lines, "taxAmount", toPlainAmount(receipt.getTaxAmount()), true);
        addJsonLine(lines, "totalChargedAmount", toPlainAmount(receipt.getTotalChargedAmount()), true);
        addJsonLine(lines, "currency", receipt.getCurrency(), true);
        addJsonLine(lines, "billingStatus", receipt.getBillingStatus(), true);
        addJsonLine(lines, "coreCommissionChargeId", receipt.getCoreCommissionChargeId(), true);
        addJsonLine(lines, "noveltyReportFilePath", receipt.getNoveltyReportFilePath(), true);
        addJsonLine(lines, "clearingFileName", receipt.getClearingFileName(), true);
        addJsonLine(lines, "clearingFilePath", receipt.getClearingFilePath(), true);
        addJsonLine(lines, "generatedAt", receipt.getGeneratedAt(), true);
        int lastIndex = lines.size() - 1;
        lines.set(lastIndex, lines.get(lastIndex).replaceFirst(",$", ""));
        lines.add("}");
        return String.join(System.lineSeparator(), lines) + System.lineSeparator();
    }

    private void addJsonLine(List<String> lines, String fieldName, Object value, Boolean quoteValue) {
        String jsonValue;
        if (value == null) {
            jsonValue = "null";
        } else if (Boolean.TRUE.equals(quoteValue)) {
            jsonValue = "\"" + escapeJson(value.toString()) + "\"";
        } else {
            jsonValue = value.toString();
        }
        lines.add("  \"" + fieldName + "\": " + jsonValue + ",");
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String toPlainAmount(BigDecimal amount) {
        return amount == null ? null : amount.toPlainString();
    }

    private String csv(Object value) {
        if (value == null) {
            return "";
        }
        String text = value.toString();
        boolean mustQuote = text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r");
        String escaped = text.replace("\"", "\"\"");
        return mustQuote ? "\"" + escaped + "\"" : escaped;
    }
}
