package com.banquito.switchpagos.reporting.service.impl;

import com.banquito.switchpagos.reporting.dto.event.OffUsClearingIncludedEvent;
import com.banquito.switchpagos.reporting.service.OffUsClearingEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitOffUsClearingEventPublisher implements OffUsClearingEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String clearingExchange;
    private final String offUsIncludedRoutingKey;

    public RabbitOffUsClearingEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbit.exchange.clearing}") String clearingExchange,
            @Value("${rabbit.routing-key.off-us-included}") String offUsIncludedRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.clearingExchange = clearingExchange;
        this.offUsIncludedRoutingKey = offUsIncludedRoutingKey;
    }

    @Override
    public void publishIncluded(OffUsClearingIncludedEvent event) {
        rabbitTemplate.convertAndSend(clearingExchange, offUsIncludedRoutingKey, event);
    }
}
