package com.gino.food.order.service.domain;

import com.gino.food.domain.event.publisher.DomainEventPublisher;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.entity.Restaurant;
import com.gino.food.order.service.domain.event.OrderCancelledEvent;
import com.gino.food.order.service.domain.event.OrderCreatedEvent;
import com.gino.food.order.service.domain.event.OrderPaidEvent;
import java.util.List;

public interface OrderDomainService {

  OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant,
      DomainEventPublisher<OrderCreatedEvent> orderCreatedEventDomainEventPublisher);

  OrderPaidEvent payOrder(Order order,
      DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher);

  void approveOrder(Order order);

  OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages,
      DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher);

  void cancelOrder(Order order, List<String> failureMessages);
}
