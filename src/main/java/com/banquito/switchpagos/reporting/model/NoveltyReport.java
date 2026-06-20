package com.banquito.switchpagos.reporting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"REPORTE_NOVEDADES\"")
public class NoveltyReport {

    @Id
    @Column(name = "\"ID_REPORTE_NOVEDADES\"", nullable = false)
    private UUID noveltyReportId;

    @Column(name = "\"ID_EVENTO_ORIGEN\"", nullable = false)
    private UUID sourceEventId;

    @Column(name = "\"ID_LOTE\"", nullable = false, unique = true)
    private UUID batchId;

    @Column(name = "\"ID_COBRO_COMISION\"")
    private UUID billingId;

    @Column(name = "\"ID_CORRELACION\"", nullable = false)
    private UUID correlationId;

    @Column(name = "\"ESTADO\"", nullable = false)
    private String status;

    @Column(name = "\"NOMBRE_ARCHIVO\"", nullable = false)
    private String fileName;

    @Column(name = "\"RUTA_ARCHIVO\"", nullable = false)
    private String filePath;

    @Column(name = "\"TOTAL_LINEAS\"", nullable = false)
    private Integer totalLines;

    @Column(name = "\"FECHA_GENERACION\"", nullable = false)
    private OffsetDateTime generatedAt;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"FECHA_ACTUALIZACION\"", nullable = false)
    private OffsetDateTime updatedAt;

    public NoveltyReport() {
    }

    public NoveltyReport(UUID noveltyReportId) {
        this.noveltyReportId = noveltyReportId;
    }

    public UUID getNoveltyReportId() { return noveltyReportId; }
    public void setNoveltyReportId(UUID noveltyReportId) { this.noveltyReportId = noveltyReportId; }
    public UUID getSourceEventId() { return sourceEventId; }
    public void setSourceEventId(UUID sourceEventId) { this.sourceEventId = sourceEventId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getBillingId() { return billingId; }
    public void setBillingId(UUID billingId) { this.billingId = billingId; }
    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Integer getTotalLines() { return totalLines; }
    public void setTotalLines(Integer totalLines) { this.totalLines = totalLines; }
    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof NoveltyReport that)) { return false; }
        return Objects.equals(noveltyReportId, that.noveltyReportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noveltyReportId);
    }

    @Override
    public String toString() {
        return "NoveltyReport{noveltyReportId=" + noveltyReportId + ", batchId=" + batchId + ", status=" + status + "}";
    }
}
