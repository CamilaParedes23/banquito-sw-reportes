package com.banquito.switchpagos.reporting.service;

import com.banquito.switchpagos.reporting.dto.event.BillingCompletedEvent;

public interface FinalReportingService {

    void processBillingCompleted(BillingCompletedEvent event);
}
