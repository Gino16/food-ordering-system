package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;
import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

  private final DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher;

  public OrderPaidEvent(Order order, ZonedDateTime createdEvent,
      DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher) {
    super(order, createdEvent);
    this.orderPaidEventDomainEventPublisher = orderPaidEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    orderPaidEventDomainEventPublisher.publish(this);
  }
}
