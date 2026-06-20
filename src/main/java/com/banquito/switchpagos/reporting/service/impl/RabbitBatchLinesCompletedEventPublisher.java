package com.banquito.switchpagos.reporting.service.impl;

import com.banquito.switchpagos.reporting.dto.event.BatchLinesCompletedEvent;
import com.banquito.switchpagos.reporting.service.BatchLinesCompletedEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitBatchLinesCompletedEventPublisher implements BatchLinesCompletedEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String billingExchange;
    private final String batchLinesCompletedRoutingKey;

    public RabbitBatchLinesCompletedEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbit.exchange.billing}") String billingExchange,
            @Value("${rabbit.routing-key.batch-lines-completed}") String batchLinesCompletedRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.billingExchange = billingExchange;
        this.batchLinesCompletedRoutingKey = batchLinesCompletedRoutingKey;
    }

    @Override
    public void publish(BatchLinesCompletedEvent event) {
        rabbitTemplate.convertAndSend(billingExchange, batchLinesCompletedRoutingKey, event);
    }
}
