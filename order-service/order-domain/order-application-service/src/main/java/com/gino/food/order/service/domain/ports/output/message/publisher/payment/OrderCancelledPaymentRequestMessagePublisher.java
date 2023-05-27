package com.gino.food.order.service.domain.ports.output.message.publisher.payment;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {

}
