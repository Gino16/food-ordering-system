package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import domain.event.PaymentCompletedEvent;

public interface PaymentCompletedMessagePublisher extends
    DomainEventPublisher<PaymentCompletedEvent> {

}
