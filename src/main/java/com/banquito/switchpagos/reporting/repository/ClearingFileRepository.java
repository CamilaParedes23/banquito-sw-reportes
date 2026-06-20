package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.ClearingFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClearingFileRepository extends JpaRepository<ClearingFile, UUID> {

    Optional<ClearingFile> findByBatchId(UUID batchId);
}
