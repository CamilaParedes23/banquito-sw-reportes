package com.banquito.switchpagos.reporting.listener;

import com.banquito.switchpagos.reporting.dto.event.PaymentLineRequestedEvent;
import com.banquito.switchpagos.reporting.service.LineConsolidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentLineRequestedObserverListener {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentLineRequestedObserverListener.class);

    private final LineConsolidationService lineConsolidationService;

    public PaymentLineRequestedObserverListener(LineConsolidationService lineConsolidationService) {
        this.lineConsolidationService = lineConsolidationService;
    }

    @RabbitListener(queues = "${rabbit.queue.reporting.payment-lines-observer}")
    public void onPaymentLineRequested(PaymentLineRequestedEvent event) {
        if (event == null) {
            LOG.warn("Evento PaymentLineRequestedEvent nulo ignorado por observador de reporting");
            return;
        }
        LOG.info("Linea esperada observada. batchId={}, lineId={}", event.getBatchId(), event.getLineId());
        lineConsolidationService.observePaymentLine(event);
    }
}
