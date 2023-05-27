package com.gino.food.order.service.domain;

import com.gino.food.order.service.domain.dto.create.CreateOrderCommand;
import com.gino.food.order.service.domain.dto.create.CreateOrderResponse;
import com.gino.food.order.service.domain.entity.Customer;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.entity.Restaurant;
import com.gino.food.order.service.domain.event.OrderCreatedEvent;
import com.gino.food.order.service.domain.exception.OrderDomainException;
import com.gino.food.order.service.domain.mapper.OrderDataMapper;
import com.gino.food.order.service.domain.ports.output.repository.CustomerRepository;
import com.gino.food.order.service.domain.ports.output.repository.OrderRepository;
import com.gino.food.order.service.domain.ports.output.repository.RestaurantRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCreateCommandHandler {

  private final OrderDomainService orderDomainService;
  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final RestaurantRepository restaurantRepository;
  private final OrderDataMapper orderDataMapper;
  private final ApplicationDomainEventPublisher applicationDomainEventPublisher;
  @Transactional
  public CreateOrderResponse createOrderResponse(CreateOrderCommand createOrderCommand) {
    checkCustomer(createOrderCommand.getCustomerId());
    Restaurant restaurant = checkRestaurant(createOrderCommand);
    Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
    OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order,
        restaurant);
    Order orderResult = saveOrder(order);
    log.info("Order is created with id: {}", orderResult.getId().getValue());
    applicationDomainEventPublisher.publish(orderCreatedEvent);
    return orderDataMapper.orderToCreateOrderResponse(orderResult);
  }

  private void checkCustomer(UUID customerId) {
    Optional<Customer> customerOpt = customerRepository.findCustomer(customerId);
    if (customerOpt.isEmpty()) {
      log.warn("Could not find the customer with customer id: {}", customerId);
      throw new OrderDomainException("Could not find the customer with customer id: " + customerId);
    }
  }


  private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
    return restaurantRepository.findRestaurantInformation(
        orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)
    ).orElseThrow(() -> {
      log.warn("Could not find restaurant with restaurant id: {}",
          createOrderCommand.getRestaurantId());
      return new OrderDomainException(
          "Could not find restaurant with restaurant id " + createOrderCommand.getRestaurantId());
    });
  }

  private Order saveOrder(Order order) {
    return Optional.ofNullable(orderRepository.save(order))
        .orElseThrow(() -> new OrderDomainException("Could not save order!"));
  }
}
