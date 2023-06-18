package com.gino.food.restaurant.service.domain.mapper;

import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.OrderId;
import com.gino.food.domain.valueobject.OrderStatus;
import com.gino.food.domain.valueobject.RestaurantId;
import com.gino.food.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.gino.food.restaurant.service.domain.entity.OrderDetail;
import com.gino.food.restaurant.service.domain.entity.Product;
import com.gino.food.restaurant.service.domain.entity.Restaurant;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataMapper {

  public Restaurant restaurantApprovalRequestToRestaurant(
      RestaurantApprovalRequest restaurantApprovalRequest) {
    return Restaurant.Builder.builder()
        .restaurantId(
            new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
        .orderDetail(OrderDetail.Builder.builder()
            .id(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
            .products(restaurantApprovalRequest.getProducts().stream().map(
                    product -> Product.Builder.builder()
                        .id(product.getId())
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList()))
            .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
            .orderStatus(
                OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
            .build())
        .build();
  }
}
