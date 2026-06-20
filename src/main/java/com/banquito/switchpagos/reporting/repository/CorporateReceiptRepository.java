package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.CorporateReceipt;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporateReceiptRepository extends JpaRepository<CorporateReceipt, UUID> {

    Optional<CorporateReceipt> findByBatchId(UUID batchId);

    Boolean existsByBatchId(UUID batchId);
}
