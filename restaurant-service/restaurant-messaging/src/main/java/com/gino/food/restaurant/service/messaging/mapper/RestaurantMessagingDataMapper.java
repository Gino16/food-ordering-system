package com.gino.food.restaurant.service.messaging.mapper;


import com.gino.food.domain.valueobject.ProductId;
import com.gino.food.domain.valueobject.RestaurantOrderStatus;
import com.gino.food.kafka.order.avro.model.OrderApprovalStatus;
import com.gino.food.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.gino.food.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.gino.food.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.gino.food.restaurant.service.domain.entity.Product;
import com.gino.food.restaurant.service.domain.event.OrderApprovedEvent;
import com.gino.food.restaurant.service.domain.event.OrderRejectedEvent;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMessagingDataMapper {

  public RestaurantApprovalResponseAvroModel
  orderApprovedEventToRestaurantApprovalResponseAvroModel(OrderApprovedEvent orderApprovedEvent) {
    return RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(orderApprovedEvent.getOrderApproval().getOrderId().getValue().toString())
        .setRestaurantId(orderApprovedEvent.getRestaurantId().getValue().toString())
        .setCreatedAt(orderApprovedEvent.getCreatedAt().toInstant())
        .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovedEvent.
            getOrderApproval().getApprovalStatus().name()))
        .setFailureMessages(orderApprovedEvent.getFailureMessages())
        .build();
  }

  public RestaurantApprovalResponseAvroModel
  orderRejectedEventToRestaurantApprovalResponseAvroModel(OrderRejectedEvent orderRejectedEvent) {
    return RestaurantApprovalResponseAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(orderRejectedEvent.getOrderApproval().getOrderId().getValue().toString())
        .setRestaurantId(orderRejectedEvent.getRestaurantId().getValue().toString())
        .setCreatedAt(orderRejectedEvent.getCreatedAt().toInstant())
        .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderRejectedEvent.
            getOrderApproval().getApprovalStatus().name()))
        .setFailureMessages(orderRejectedEvent.getFailureMessages())
        .build();
  }

  public RestaurantApprovalRequest
  restaurantApprovalRequestAvroModelToRestaurantApproval(RestaurantApprovalRequestAvroModel
      restaurantApprovalRequestAvroModel) {
    return RestaurantApprovalRequest.builder()
        .id(restaurantApprovalRequestAvroModel.getId())
        .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
        .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
        .orderId(restaurantApprovalRequestAvroModel.getOrderId())
        .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
            .getRestaurantOrderStatus().name()))
        .products(restaurantApprovalRequestAvroModel.getProducts()
            .stream().map(avroModel ->
                Product.Builder.builder()
                    .id(new ProductId(UUID.fromString(avroModel.getId())))
                    .quantity(avroModel.getQuantity())
                    .build())
            .collect(Collectors.toList()))
        .price(restaurantApprovalRequestAvroModel.getPrice())
        .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
        .build();
  }
}
