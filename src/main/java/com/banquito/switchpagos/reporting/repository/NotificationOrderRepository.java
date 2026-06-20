package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.NotificationOrder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationOrderRepository extends JpaRepository<NotificationOrder, UUID> {

    Optional<NotificationOrder> findByLineId(UUID lineId);

    List<NotificationOrder> findByBatchId(UUID batchId);
}
