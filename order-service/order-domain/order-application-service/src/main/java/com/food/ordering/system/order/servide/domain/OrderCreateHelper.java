package com.food.ordering.system.order.servide.domain;

import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.servide.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.servide.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.servide.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.servide.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.servide.domain.ports.output.repository.RestaurantRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHelper {

  private final OrderDomainService orderDomainService;
  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final RestaurantRepository restaurantRepository;
  private final OrderDataMapper orderDataMapper;

  @Transactional
  public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
    checkCustomer(createOrderCommand.getCustomerId());
    Restaurant restaurant = checkRestaurant(createOrderCommand);
    Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
    OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order,
        restaurant);
    saveOrder(order);
    log.info("Order is created with id: {}", order.getId().getValue());
    return orderCreatedEvent;
  }

  private void checkCustomer(UUID customerId) {
    var customer = customerRepository.findCustomer(customerId);
    if (customer.isEmpty()) {
      log.warn("Could not find customer with customer id: {}", customerId);
      throw new OrderDomainException("Could not find customer with customer id: " + customerId);
    }
  }


  private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
    Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(
        restaurant);
    if (optionalRestaurant.isEmpty()) {
      log.warn("Could not find restaurant with restaurant id: {}",
          createOrderCommand.getRestaurantId());
      throw new OrderDomainException(
          "Could not find restaurant with restaurant id: " + createOrderCommand.getRestaurantId());
    }
    return null;
  }

  private Order saveOrder(Order order) {
    Order orderResult = orderRepository.save(order);
    if (orderResult == null) {
      log.warn("Could not save the order!!");
      throw new OrderDomainException("Could not save the order!!");
    }
    log.info("Order is save with id: {}", orderResult.getId().getValue());
    return orderResult;
  }

}
