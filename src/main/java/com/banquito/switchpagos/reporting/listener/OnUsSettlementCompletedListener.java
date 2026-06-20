package com.banquito.switchpagos.reporting.listener;

import com.banquito.switchpagos.reporting.dto.event.OnUsSettlementCompletedEvent;
import com.banquito.switchpagos.reporting.service.LineConsolidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OnUsSettlementCompletedListener {

    private static final Logger LOG = LoggerFactory.getLogger(OnUsSettlementCompletedListener.class);

    private final LineConsolidationService lineConsolidationService;

    public OnUsSettlementCompletedListener(LineConsolidationService lineConsolidationService) {
        this.lineConsolidationService = lineConsolidationService;
    }

    @RabbitListener(queues = "${rabbit.queue.reporting.on-us-completed}")
    public void onOnUsSettlementCompleted(OnUsSettlementCompletedEvent event) {
        if (event == null) {
            LOG.warn("Evento OnUsSettlementCompletedEvent nulo ignorado por reporting");
            return;
        }
        LOG.info("Resultado On-Us recibido para consolidacion. batchId={}, lineId={}, status={}",
                event.getBatchId(), event.getLineId(), event.getFinalStatus());
        lineConsolidationService.registerOnUsCompleted(event);
    }
}
