package com.gino.food.order.service.messaging.mapper;

import com.gino.food.domain.valueobject.OrderApprovalStatus;
import com.gino.food.domain.valueobject.PaymentStatus;
import com.gino.food.kafka.order.avro.model.PaymentOrderStatus;
import com.gino.food.kafka.order.avro.model.PaymentRequestAvroModel;
import com.gino.food.kafka.order.avro.model.PaymentResponseAvroModel;
import com.gino.food.kafka.order.avro.model.Product;
import com.gino.food.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.gino.food.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.gino.food.kafka.order.avro.model.RestaurantOrderStatus;
import com.gino.food.order.service.domain.dto.message.PaymentResponse;
import com.gino.food.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.event.OrderCancelledEvent;
import com.gino.food.order.service.domain.event.OrderCreatedEvent;
import com.gino.food.order.service.domain.event.OrderPaidEvent;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderMessagingDataMapper {

  public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(
      OrderCreatedEvent orderCreatedEvent) {
    Order order = orderCreatedEvent.getOrder();
    return PaymentRequestAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setCustomerId(order.getCustomerId().getValue().toString())
        .setOrderId(order.getId().getValue().toString())
        .setPrice(order.getPrice().getAmount())
        .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
        .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
        .build();
  }

  public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(
      OrderCancelledEvent orderCancelledEvent) {
    Order order = orderCancelledEvent.getOrder();
    return PaymentRequestAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setCustomerId(order.getCustomerId().getValue().toString())
        .setOrderId(order.getId().getValue().toString())
        .setPrice(order.getPrice().getAmount())
        .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
        .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
        .build();
  }

  public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalAvroModel(
      OrderPaidEvent orderPaidEvent) {
    Order order = orderPaidEvent.getOrder();
    return RestaurantApprovalRequestAvroModel.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setSagaId("")
        .setOrderId(order.getId().getValue().toString())
        .setRestaurantId(order.getRestaurantId().getValue().toString())
        .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
        .setProducts(order.getItems().stream().map(orderItem ->
            Product.newBuilder()
                .setId(orderItem.getProduct().getId().getValue().toString())
                .setQuantity(orderItem.getQuantity())
                .build()
        ).collect(Collectors.toList()))
        .setPrice(order.getPrice().getAmount())
        .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
        .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
        .build();
  }

  public PaymentResponse paymentResponseAvroModelToPaymentResponse(
      PaymentResponseAvroModel paymentResponseAvroModel) {
    return PaymentResponse.builder()
        .id(paymentResponseAvroModel.getId())
        .sagaId(paymentResponseAvroModel.getSagaId())
        .paymentId(paymentResponseAvroModel.getPaymentId())
        .customerId(paymentResponseAvroModel.getCustomerId())
        .orderId(paymentResponseAvroModel.getOrderId())
        .price(paymentResponseAvroModel.getPrice())
        .createdAt(paymentResponseAvroModel.getCreatedAt())
        .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
        .failureMessages(paymentResponseAvroModel.getFailureMessages())
        .build();
  }

  public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(
      RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
    return RestaurantApprovalResponse.builder()
        .id(restaurantApprovalResponseAvroModel.getId())
        .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
        .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
        .orderId(restaurantApprovalResponseAvroModel.getOrderId())
        .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
        .orderApprovalStatus(OrderApprovalStatus.valueOf(
            restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
        .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
        .build();
  }
}
