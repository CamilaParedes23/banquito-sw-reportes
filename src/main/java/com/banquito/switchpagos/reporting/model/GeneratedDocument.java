package com.banquito.switchpagos.reporting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "\"DOCUMENTO_GENERADO\"")
public class GeneratedDocument {

    @Id
    @Column(name = "\"ID_DOCUMENTO_GENERADO\"", nullable = false)
    private UUID generatedDocumentId;

    @Column(name = "\"ID_LOTE\"", nullable = false)
    private UUID batchId;

    @Column(name = "\"TIPO_DOCUMENTO\"", nullable = false)
    private String documentType;

    @Column(name = "\"ID_DOCUMENTO\"", nullable = false)
    private UUID documentId;

    @Column(name = "\"NOMBRE_ARCHIVO\"", nullable = false)
    private String fileName;

    @Column(name = "\"RUTA_ARCHIVO\"", nullable = false)
    private String filePath;

    @Column(name = "\"ESTADO\"", nullable = false)
    private String status;

    @Column(name = "\"FECHA_GENERACION\"", nullable = false)
    private OffsetDateTime generatedAt;

    @Column(name = "\"FECHA_CREACION\"", nullable = false)
    private OffsetDateTime createdAt;

    public GeneratedDocument() {
    }

    public GeneratedDocument(UUID generatedDocumentId) {
        this.generatedDocumentId = generatedDocumentId;
    }

    public UUID getGeneratedDocumentId() { return generatedDocumentId; }
    public void setGeneratedDocumentId(UUID generatedDocumentId) { this.generatedDocumentId = generatedDocumentId; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public UUID getDocumentId() { return documentId; }
    public void setDocumentId(UUID documentId) { this.documentId = documentId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof GeneratedDocument that)) { return false; }
        return Objects.equals(generatedDocumentId, that.generatedDocumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generatedDocumentId);
    }

    @Override
    public String toString() {
        return "GeneratedDocument{generatedDocumentId=" + generatedDocumentId
                + ", batchId=" + batchId + ", documentType=" + documentType + "}";
    }
}
