package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.order.service.domain.entity.Order;
import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {

  private final Order order;
  private final ZonedDateTime createdEvent;

  public OrderEvent(Order order, ZonedDateTime createdEvent) {
    this.order = order;
    this.createdEvent = createdEvent;
  }

  public Order getOrder() {
    return order;
  }

  public ZonedDateTime getCreatedEvent() {
    return createdEvent;
  }
}
