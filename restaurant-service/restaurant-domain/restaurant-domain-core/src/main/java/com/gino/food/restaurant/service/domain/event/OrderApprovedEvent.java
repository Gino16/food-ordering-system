package com.gino.food.restaurant.service.domain.event;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.domain.valueobject.RestaurantId;
import com.gino.food.restaurant.service.domain.entity.OrderApproval;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

  private final DomainEventPublisher<OrderApprovedEvent> orderApprovalEventDomainEventPublisher;

  public OrderApprovedEvent(
      OrderApproval orderApproval,
      RestaurantId restaurantId,
      List<String> failureMessages, ZonedDateTime createdAt,
      DomainEventPublisher<OrderApprovedEvent> orderApprovalEventDomainEventPublisher) {
    super(orderApproval, restaurantId, failureMessages, createdAt);
    this.orderApprovalEventDomainEventPublisher = orderApprovalEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    orderApprovalEventDomainEventPublisher.publish(this);
  }
}
