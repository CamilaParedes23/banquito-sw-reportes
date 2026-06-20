package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.GeneratedDocument;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneratedDocumentRepository extends JpaRepository<GeneratedDocument, UUID> {

    Optional<GeneratedDocument> findByBatchIdAndDocumentType(UUID batchId, String documentType);

    List<GeneratedDocument> findByBatchId(UUID batchId);
}
