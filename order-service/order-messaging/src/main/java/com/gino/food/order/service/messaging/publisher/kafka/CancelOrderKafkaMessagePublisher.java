package com.gino.food.order.service.messaging.publisher.kafka;

import com.gino.food.kafka.order.avro.model.PaymentRequestAvroModel;
import com.gino.food.kafka.producer.KafkaMessageHelper;
import com.gino.food.kafka.producer.service.KafkaProducer;
import com.gino.food.order.service.domain.config.OrderServiceConfigData;
import com.gino.food.order.service.domain.event.OrderCancelledEvent;
import com.gino.food.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.gino.food.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CancelOrderKafkaMessagePublisher implements
    OrderCancelledPaymentRequestMessagePublisher {

  private final OrderMessagingDataMapper orderMessagingDataMapper;
  private final OrderServiceConfigData orderServiceConfigData;
  private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
  private final KafkaMessageHelper kafkaMessageHelper;

  @Override
  public void publish(OrderCancelledEvent domainEvent) {
    String orderId = domainEvent.getOrder().getId().getValue().toString();
    log.info("Received OrderCancelledEvent for order id: {}", orderId);

    try {
      PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper.orderCancelledEventToPaymentRequestAvroModel(
          domainEvent);

      kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(), orderId,
          paymentRequestAvroModel,
          kafkaMessageHelper.getKafkaCallback(
              orderServiceConfigData.getPaymentResponseTopicName(),
              paymentRequestAvroModel, orderId,
              "PaymentRequestAvroModel"));
      log.info("PaymentRequestAvroModel sent to Kafka for order id: {}",
          paymentRequestAvroModel.getOrderId());
    } catch (Exception e) {
      log.error(
          "Error while sending PaymentRequestAvroModel message to kafka with order id: {}, error: {}",
          orderId, e.getMessage());
    }
  }
}
