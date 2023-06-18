package com.gino.food.order.service.messaging.publisher.kafka;

import com.gino.food.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.gino.food.kafka.producer.KafkaMessageHelper;
import com.gino.food.kafka.producer.service.KafkaProducer;
import com.gino.food.order.service.domain.config.OrderServiceConfigData;
import com.gino.food.order.service.domain.event.OrderPaidEvent;
import com.gino.food.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.gino.food.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

  private final OrderMessagingDataMapper orderMessagingDataMapper;
  private final OrderServiceConfigData orderServiceConfigData;
  private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
  private final KafkaMessageHelper kafkaMessageHelper;

  @Override
  public void publish(OrderPaidEvent domainEvent) {
    String orderId = domainEvent.getOrder().getId().getValue().toString();
    try {
      RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper.orderPaidEventToRestaurantApprovalAvroModel(
          domainEvent);
      kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), orderId,
          restaurantApprovalRequestAvroModel,
          kafkaMessageHelper.getKafkaCallback(
              orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
              restaurantApprovalRequestAvroModel, orderId, "RestaurantApprovalRequestAvroModel")
      );
      log.info("RestaurantApprovalRequestAvroModel sent to kafka for order id: {}", orderId);
    } catch (Exception e) {
      log.error(
          "Error while sending RestaurantApprovalRequestAvroModel message to kafka with order id: {}, error: {}",
          orderId, e.getMessage());
    }
  }
}
