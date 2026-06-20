package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.BatchProcessingSummary;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchProcessingSummaryRepository extends JpaRepository<BatchProcessingSummary, UUID> {

    Optional<BatchProcessingSummary> findByBatchId(UUID batchId);
}
