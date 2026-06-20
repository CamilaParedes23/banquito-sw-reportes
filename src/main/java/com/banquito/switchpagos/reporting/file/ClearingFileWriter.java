package com.banquito.switchpagos.reporting.file;

import com.banquito.switchpagos.reporting.exception.ClearingFileGenerationException;
import com.banquito.switchpagos.reporting.model.ClearingFile;
import com.banquito.switchpagos.reporting.model.ClearingFileLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ClearingFileWriter {

    private static final String HEADER = "batchId,lineId,sequenceNumber,destinationInstitutionName,routingCode,destinationAccountNumber,beneficiaryIdentification,beneficiaryName,amount,currency,reference";

    private final Path clearingDirectory;

    public ClearingFileWriter(@Value("${switch.files.clearing-directory}") String clearingDirectory) {
        this.clearingDirectory = Path.of(clearingDirectory).normalize();
    }

    public void writeClearingFile(ClearingFile clearingFile, List<ClearingFileLine> includedLines, ClearingFileLine candidateLine) {
        List<ClearingFileLine> lines = new ArrayList<>(includedLines);
        lines.add(candidateLine);
        lines.sort(Comparator
                .comparing(ClearingFileLine::getSequenceNumber, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ClearingFileLine::getLineId));

        try {
            Files.createDirectories(clearingDirectory);
            Path targetFile = clearingDirectory.resolve(clearingFile.getFileName()).normalize();
            Path temporaryFile = clearingDirectory.resolve(clearingFile.getFileName() + ".tmp").normalize();
            List<String> content = new ArrayList<>();
            content.add(HEADER);
            for (ClearingFileLine line : lines) {
                content.add(toCsvLine(line));
            }
            Files.write(temporaryFile, content, StandardCharsets.UTF_8);
            moveFile(temporaryFile, targetFile);
        } catch (IOException exception) {
            throw new ClearingFileGenerationException("No se pudo generar el archivo de compensacion Off-Us", exception);
        }
    }

    private void moveFile(Path temporaryFile, Path targetFile) throws IOException {
        try {
            Files.move(temporaryFile, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String toCsvLine(ClearingFileLine line) {
        return csv(line.getBatchId()) + "," +
                csv(line.getLineId()) + "," +
                csv(line.getSequenceNumber()) + "," +
                csv(line.getDestinationInstitutionName()) + "," +
                csv(line.getRoutingCode()) + "," +
                csv(line.getDestinationAccountNumber()) + "," +
                csv(line.getBeneficiaryIdentification()) + "," +
                csv(line.getBeneficiaryName()) + "," +
                csv(toPlainAmount(line.getAmount())) + "," +
                csv(line.getCurrency()) + "," +
                csv(line.getReference());
    }

    private String toPlainAmount(BigDecimal amount) {
        return amount == null ? "" : amount.toPlainString();
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
