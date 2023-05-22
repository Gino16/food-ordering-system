package com.gino.food.order.service.domain.ports.output.repository.message.publisher.payment;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatePaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
