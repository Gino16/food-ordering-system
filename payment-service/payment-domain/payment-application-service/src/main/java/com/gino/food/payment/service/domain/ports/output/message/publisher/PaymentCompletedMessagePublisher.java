package com.gino.food.payment.service.domain.ports.output.message.publisher;

import com.food.gino.payment.service.domain.event.PaymentCompletedEvent;
import com.gino.food.domain.event.publisher.DomainEventPublisher;

public interface PaymentCompletedMessagePublisher extends DomainEventPublisher<PaymentCompletedEvent> {

}
