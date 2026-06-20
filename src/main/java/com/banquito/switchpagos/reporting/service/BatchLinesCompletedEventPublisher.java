package com.banquito.switchpagos.reporting.service;

import com.banquito.switchpagos.reporting.dto.event.BatchLinesCompletedEvent;

public interface BatchLinesCompletedEventPublisher {

    void publish(BatchLinesCompletedEvent event);
}
