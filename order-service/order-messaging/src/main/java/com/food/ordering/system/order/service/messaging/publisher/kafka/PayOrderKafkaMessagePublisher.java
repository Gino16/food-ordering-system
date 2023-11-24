package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

  private final OrderMessagingDataMapper orderMessagingDataMapper;
  private final OrderServiceConfigData orderServiceConfigData;
  private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
  private final KafkaMessageHelper kafkaMessageHelper;

  @Override
  public void publish(OrderPaidEvent domain) {
    String orderId = domain.getOrder().getId().getValue().toString();
    try {
      RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel =
          orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(domain);

      kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), orderId,
          restaurantApprovalRequestAvroModel,
          kafkaMessageHelper.getKafkaCallback(
              orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
              restaurantApprovalRequestAvroModel, orderId, "RestaurantApprovalRequestAvroModel"));
      log.info("RestaurantApprovalRequestAvroModel sent to kafka for order id: {}", orderId);
    } catch (Exception e) {
      log.error(
          "Error while sending RestaurantApprovalRequestAvroModel message to kafka with order id: {}, "
              + "error: {}", orderId, e.getMessage());
    }
  }
}
