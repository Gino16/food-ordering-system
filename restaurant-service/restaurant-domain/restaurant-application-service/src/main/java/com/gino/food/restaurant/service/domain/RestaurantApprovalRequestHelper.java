package com.gino.food.restaurant.service.domain;

import com.gino.food.domain.valueobject.OrderId;
import com.gino.food.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.gino.food.restaurant.service.domain.entity.Restaurant;
import com.gino.food.restaurant.service.domain.event.OrderApprovalEvent;
import com.gino.food.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.gino.food.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.gino.food.restaurant.service.domain.ports.output.message.listener.OrderApprovedMessagePublisher;
import com.gino.food.restaurant.service.domain.ports.output.message.listener.OrderRejectedMessagePublisher;
import com.gino.food.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.gino.food.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantApprovalRequestHelper {

  private final RestaurantDomainService restaurantDomainService;
  private final RestaurantDataMapper restaurantDataMapper;
  private final RestaurantRepository restaurantRepository;
  private final OrderApprovalRepository orderApprovalRepository;
  private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
  private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

  @Transactional
  public OrderApprovalEvent persistOrderApproval(
      RestaurantApprovalRequest restaurantApprovalRequest) {
    log.info("Processing restaurant approval for order id: {}",
        restaurantApprovalRequest.getOrderId());
    List<String> failureMessages = new ArrayList<>();
    ;
    Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
    OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant,
        failureMessages,
        orderApprovedMessagePublisher, orderRejectedMessagePublisher);
    orderApprovalRepository.save(restaurant.getOrderApproval());
    return orderApprovalEvent;
  }

  private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
    Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(
        restaurantApprovalRequest);
    Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInformation(
        restaurant);
    if (restaurantOptional.isPresent()) {
      restaurant = restaurantOptional.get();
    } else {
      log.error("Restaurant not found for restaurant id: {}",
          restaurantApprovalRequest.getRestaurantId());
      throw new RestaurantNotFoundException("Restaurant not found");
    }
    Restaurant restaurantEntity = restaurantOptional.get();
    restaurantEntity.setActive(restaurantEntity.isActive());
    restaurant.getOrderDetail().getProducts().forEach(product -> {
      restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
        if (p.getId().equals(product.getId())) {
          product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(),
              p.isAvailable());
        }
      });
    });
    restaurant.getOrderDetail()
        .setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));
    return restaurant;
  }
}
