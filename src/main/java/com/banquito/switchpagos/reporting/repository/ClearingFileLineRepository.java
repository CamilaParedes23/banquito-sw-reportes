package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.ClearingFileLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClearingFileLineRepository extends JpaRepository<ClearingFileLine, UUID> {

    Optional<ClearingFileLine> findByLineId(UUID lineId);

    List<ClearingFileLine> findByBatchIdAndStatusOrderBySequenceNumberAsc(UUID batchId, String status);
}
