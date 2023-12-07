package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHelper {
  private final OrderRepository orderRepository;

  Order findOrder(String orderId) {
    Optional<Order> orderResponse = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
    if (orderResponse.isEmpty()) {
      log.error("Order with id: {} could not be found!", orderId);
      throw new OrderNotFoundException("Order with id: " + orderId + " could not be found!");
    }
    return orderResponse.get();
  }

  void saveOrder(Order order) {
    orderRepository.save(order);
  }
}
