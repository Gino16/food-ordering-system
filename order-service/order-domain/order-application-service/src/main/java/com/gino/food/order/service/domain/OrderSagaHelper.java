package com.gino.food.order.service.domain;

import com.gino.food.domain.valueobject.OrderId;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.exception.OrderNotFoundException;
import com.gino.food.order.service.domain.ports.output.repository.OrderRepository;
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
    return orderRepository.findById(new OrderId(UUID.fromString(orderId)))
        .orElseThrow(() -> new OrderNotFoundException("Order not found"));
  }

  void saveOrder(Order order) {
    orderRepository.save(order);
  }
}
