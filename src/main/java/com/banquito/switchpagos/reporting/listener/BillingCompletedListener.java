package com.banquito.switchpagos.reporting.listener;

import com.banquito.switchpagos.reporting.dto.event.BillingCompletedEvent;
import com.banquito.switchpagos.reporting.service.FinalReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BillingCompletedListener {

    private static final Logger LOG = LoggerFactory.getLogger(BillingCompletedListener.class);

    private final FinalReportingService finalReportingService;

    public BillingCompletedListener(FinalReportingService finalReportingService) {
        this.finalReportingService = finalReportingService;
    }

    @RabbitListener(queues = "${rabbit.queue.reporting.billing-completed}")
    public void onBillingCompleted(BillingCompletedEvent event) {
        if (event == null) {
            LOG.warn("BillingCompletedEvent nulo ignorado por reporting final");
            return;
        }
        LOG.info("BillingCompletedEvent recibido. batchId={}, billingId={}", event.getBatchId(), event.getBillingId());
        finalReportingService.processBillingCompleted(event);
    }
}
