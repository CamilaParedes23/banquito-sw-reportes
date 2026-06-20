package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.NoveltyReport;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoveltyReportRepository extends JpaRepository<NoveltyReport, UUID> {

    Optional<NoveltyReport> findByBatchId(UUID batchId);

    Boolean existsByBatchId(UUID batchId);
}
