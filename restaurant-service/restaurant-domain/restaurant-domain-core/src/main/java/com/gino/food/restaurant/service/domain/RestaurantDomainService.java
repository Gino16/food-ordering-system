package com.gino.food.restaurant.service.domain;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.restaurant.service.domain.entity.Restaurant;
import com.gino.food.restaurant.service.domain.event.OrderApprovalEvent;
import com.gino.food.restaurant.service.domain.event.OrderApprovedEvent;
import com.gino.food.restaurant.service.domain.event.OrderRejectedEvent;
import java.util.List;

public interface RestaurantDomainService {

  OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages,
      DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
      DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);

}
