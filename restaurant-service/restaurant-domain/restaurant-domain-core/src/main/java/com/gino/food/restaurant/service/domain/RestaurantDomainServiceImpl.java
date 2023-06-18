package com.gino.food.restaurant.service.domain;

import static com.gino.food.domain.DomainConstants.UTC;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.domain.valueobject.OrderApprovalStatus;
import com.gino.food.restaurant.service.domain.entity.Restaurant;
import com.gino.food.restaurant.service.domain.event.OrderApprovalEvent;
import com.gino.food.restaurant.service.domain.event.OrderApprovedEvent;
import com.gino.food.restaurant.service.domain.event.OrderRejectedEvent;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

  @Override
  public OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages,
      DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
      DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
    restaurant.validateOrder(failureMessages);
    log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
    if (failureMessages.isEmpty()) {
      log.info("Order approved with id: {}", restaurant.getOrderDetail().getId().getValue());
      restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
      return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(),
          failureMessages, ZonedDateTime.now(ZoneId.of(UTC)),
          orderApprovedEventDomainEventPublisher);
    } else {
      log.info("Order rejected with id: {}", restaurant.getOrderDetail().getId().getValue());
      restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
      return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(),
          failureMessages, ZonedDateTime.now(ZoneId.of(UTC)),
          orderRejectedEventDomainEventPublisher);
    }
  }
}
