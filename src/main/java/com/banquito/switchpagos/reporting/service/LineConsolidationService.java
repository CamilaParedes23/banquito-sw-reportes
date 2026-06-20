package com.banquito.switchpagos.reporting.service;

import com.banquito.switchpagos.reporting.dto.event.OffUsClearingIncludedEvent;
import com.banquito.switchpagos.reporting.dto.event.OnUsSettlementCompletedEvent;
import com.banquito.switchpagos.reporting.dto.event.PaymentLineRejectedEvent;
import com.banquito.switchpagos.reporting.dto.event.PaymentLineRequestedEvent;
import com.banquito.switchpagos.reporting.model.ClearingFileLine;

public interface LineConsolidationService {

    void observePaymentLine(PaymentLineRequestedEvent event);

    void registerOnUsCompleted(OnUsSettlementCompletedEvent event);

    void registerOffUsIncluded(OffUsClearingIncludedEvent event);

    void registerOffUsFailure(ClearingFileLine line);

    void registerRejected(PaymentLineRejectedEvent event);
}
