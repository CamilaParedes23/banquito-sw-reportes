package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.LineProcessingResult;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineProcessingResultRepository extends JpaRepository<LineProcessingResult, UUID> {

    Optional<LineProcessingResult> findByLineId(UUID lineId);

    List<LineProcessingResult> findByBatchId(UUID batchId);

    Long countByBatchId(UUID batchId);
}
