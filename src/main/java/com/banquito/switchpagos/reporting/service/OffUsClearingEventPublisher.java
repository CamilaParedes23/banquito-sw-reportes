package com.banquito.switchpagos.reporting.service;

import com.banquito.switchpagos.reporting.dto.event.OffUsClearingIncludedEvent;

public interface OffUsClearingEventPublisher {

    void publishIncluded(OffUsClearingIncludedEvent event);
}
