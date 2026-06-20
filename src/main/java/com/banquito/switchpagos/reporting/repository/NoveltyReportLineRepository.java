package com.banquito.switchpagos.reporting.repository;

import com.banquito.switchpagos.reporting.model.NoveltyReportLine;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoveltyReportLineRepository extends JpaRepository<NoveltyReportLine, UUID> {

    Optional<NoveltyReportLine> findByLineId(UUID lineId);

    List<NoveltyReportLine> findByBatchId(UUID batchId);

    List<NoveltyReportLine> findByBatchIdOrderBySequenceNumberAsc(UUID batchId);
}
