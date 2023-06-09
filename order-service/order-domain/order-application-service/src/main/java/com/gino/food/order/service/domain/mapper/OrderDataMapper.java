package com.gino.food.order.service.domain.mapper;

import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.ProductId;
import com.gino.food.domain.valueobject.RestaurantId;
import com.gino.food.order.service.domain.dto.create.CreateOrderCommand;
import com.gino.food.order.service.domain.dto.create.CreateOrderResponse;
import com.gino.food.order.service.domain.dto.create.OrderAddress;
import com.gino.food.order.service.domain.dto.track.TrackOrderResponse;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.entity.OrderItem;
import com.gino.food.order.service.domain.entity.Product;
import com.gino.food.order.service.domain.entity.Restaurant;
import com.gino.food.order.service.domain.valueobject.StreetAddress;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderDataMapper {

  public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
    return Restaurant.Builder.builder()
        .id(new RestaurantId(createOrderCommand.getRestaurantId()))
        .products(createOrderCommand.getItems().stream()
            .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
            .collect(Collectors.toList()))
        .build();
  }

  public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
    return Order.Builder.builder()
        .customerId(new CustomerId(createOrderCommand.getCustomerId()))
        .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
        .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
        .price(new Money(createOrderCommand.getPrice()))
        .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
        .build();
  }

  public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
    return CreateOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .message(message)
        .build();
  }

  public TrackOrderResponse orderToTrackOrderResponse(Order order) {
    return TrackOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .failureMessages(order.getFailureMessages())
        .build();
  }

  private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
    return new StreetAddress(
        UUID.randomUUID(),
        orderAddress.getStreet(),
        orderAddress.getPostalCode(),
        orderAddress.getCity()
    );
  }

  private List<OrderItem> orderItemsToOrderItemEntities(
      List<com.gino.food.order.service.domain.dto.create.OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> OrderItem.Builder.builder()
            .product(new Product(new ProductId(orderItem.getProductId())))
            .price(new Money(orderItem.getPrice()))
            .quantity(orderItem.getQuantity())
            .subTotal(new Money(orderItem.getSubTotal()))
            .build()
        ).collect(Collectors.toList());
  }


}
