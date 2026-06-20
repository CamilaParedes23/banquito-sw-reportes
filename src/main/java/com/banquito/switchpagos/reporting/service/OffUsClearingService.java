package com.banquito.switchpagos.reporting.service;

import com.banquito.switchpagos.reporting.dto.event.PaymentLineRoutedOffUsEvent;

public interface OffUsClearingService {

    void includeOffUsLine(PaymentLineRoutedOffUsEvent event);
}
