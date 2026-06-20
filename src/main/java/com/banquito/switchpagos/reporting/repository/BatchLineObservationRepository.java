package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.BatchLineObservation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchLineObservationRepository extends JpaRepository<BatchLineObservation, UUID> {

    Optional<BatchLineObservation> findByLineId(UUID lineId);

    List<BatchLineObservation> findByBatchId(UUID batchId);

    Long countByBatchId(UUID batchId);
}
