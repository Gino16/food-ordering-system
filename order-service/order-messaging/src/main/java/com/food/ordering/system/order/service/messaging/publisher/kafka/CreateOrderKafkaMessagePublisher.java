package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.order.servide.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.servide.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements
    OrderCreatedPaymentRequestMessagePublisher {

  private final OrderMessagingDataMapper orderMessagingDataMapper;
  private final OrderServiceConfigData orderServiceConfigData;
  private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
  private OrderKafkaMessageHelper orderKafkaMessageHelper;

  @Override
  public void publish(OrderCreatedEvent domain) {
    String orderId = domain.getOrder().getId().getValue().toString();
    log.info("Received OrderCreatedEvent for order id: {}", orderId);
    try {
      PaymentRequestAvroModel paymentRequestAvroModel =
          orderMessagingDataMapper.orderCreatedEventToPaymentRequestAvroModel(domain);

      kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(), orderId,
          paymentRequestAvroModel,
          orderKafkaMessageHelper.getKafkaCallback(
              orderServiceConfigData.getPaymentRequestTopicName(),
              paymentRequestAvroModel, orderId, "PaymentRequestAvroModel"));

      log.info("PaymentRequestAvroModel sent to Kafka for order id: {}",
          paymentRequestAvroModel.getOrderId());
    } catch (Exception e) {
      log.error("Error while sending PaymentRequestAvroModel message to kafka with order id: {}, "
          + "error: {}", orderId, e.getMessage());
    }
  }


}