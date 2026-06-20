package com.banquito.switchpagos.reporting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"ARCHIVO_COMPENSACION\"")
public class ClearingFile {

    @Id
    @Column(name = "\"ID_ARCHIVO_COMPENSACION\"")
    private UUID clearingFileId;

    @Column(name = "\"ID_LOTE\"", nullable = false, unique = true)
    private UUID batchId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"NOMBRE_ARCHIVO\"", nullable = false, length = 160)
    private String fileName;

    @Column(name = "\"RUTA_RELATIVA\"", nullable = false, length = 240)
    private String relativePath;

    @Column(name = "\"ESTADO\"", nullable = false, length = 40)
    private String status;

    @Column(name = "\"TOTAL_LINEAS\"", nullable = false)
    private Integer totalLines;

    @Column(name = "\"MONTO_TOTAL\"", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "\"MONEDA\"", nullable = false, length = 3)
    private String currency;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public ClearingFile() {
    }

    public ClearingFile(UUID clearingFileId) {
        this.clearingFileId = clearingFileId;
    }

    public UUID getClearingFileId() { return clearingFileId; }
    public void setClearingFileId(UUID clearingFileId) { this.clearingFileId = clearingFileId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getRelativePath() { return relativePath; }
    public void setRelativePath(String relativePath) { this.relativePath = relativePath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTotalLines() { return totalLines; }
    public void setTotalLines(Integer totalLines) { this.totalLines = totalLines; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ClearingFile that)) {
            return false;
        }
        return Objects.equals(clearingFileId, that.clearingFileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clearingFileId);
    }

    @Override
    public String toString() {
        return "ClearingFile{" +
                "clearingFileId=" + clearingFileId +
                ", batchId=" + batchId +
                ", fileName='" + fileName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
