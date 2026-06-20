package com.banquito.switchpagos.reporting.listener;

import com.banquito.switchpagos.reporting.dto.event.PaymentLineRoutedOffUsEvent;
import com.banquito.switchpagos.reporting.service.OffUsClearingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentLineRoutedOffUsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentLineRoutedOffUsListener.class);

    private final OffUsClearingService offUsClearingService;

    public PaymentLineRoutedOffUsListener(OffUsClearingService offUsClearingService) {
        this.offUsClearingService = offUsClearingService;
    }

    @RabbitListener(queues = "${rabbit.queue.clearing.off-us}")
    public void onPaymentLineRoutedOffUs(PaymentLineRoutedOffUsEvent event) {
        if (event == null) {
            LOGGER.warn("Evento Off-Us nulo recibido en cola de clearing");
            return;
        }
        LOGGER.info("Evento Off-Us recibido. batchId={}, lineId={}", event.getBatchId(), event.getLineId());
        offUsClearingService.includeOffUsLine(event);
    }
}
