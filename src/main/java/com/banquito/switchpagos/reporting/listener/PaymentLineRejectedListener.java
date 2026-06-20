package com.banquito.switchpagos.reporting.listener;

import com.banquito.switchpagos.reporting.dto.event.PaymentLineRejectedEvent;
import com.banquito.switchpagos.reporting.service.LineConsolidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentLineRejectedListener {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentLineRejectedListener.class);

    private final LineConsolidationService lineConsolidationService;

    public PaymentLineRejectedListener(LineConsolidationService lineConsolidationService) {
        this.lineConsolidationService = lineConsolidationService;
    }

    @RabbitListener(queues = "${rabbit.queue.reporting.line-rejected}")
    public void onPaymentLineRejected(PaymentLineRejectedEvent event) {
        if (event == null) {
            LOG.warn("Evento PaymentLineRejectedEvent nulo ignorado por reporting");
            return;
        }
        LOG.info("Linea rechazada recibida para consolidacion. batchId={}, lineId={}, code={}",
                event.getBatchId(), event.getLineId(), event.getRejectionCode());
        lineConsolidationService.registerRejected(event);
    }
}
