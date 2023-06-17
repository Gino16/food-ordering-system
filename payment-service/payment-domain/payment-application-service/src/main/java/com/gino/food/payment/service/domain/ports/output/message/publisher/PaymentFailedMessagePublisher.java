package com.gino.food.payment.service.domain.ports.output.message.publisher;

import com.food.gino.payment.service.domain.event.PaymentFailedEvent;
import com.gino.food.domain.event.publisher.DomainEventPublisher;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {

}
