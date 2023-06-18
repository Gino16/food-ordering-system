package com.gino.food.payment.service.messaging.publisher.kafka;

import com.food.gino.payment.service.domain.event.PaymentCancelledEvent;
import com.gino.food.kafka.order.avro.model.PaymentResponseAvroModel;
import com.gino.food.kafka.producer.KafkaMessageHelper;
import com.gino.food.kafka.producer.service.KafkaProducer;
import com.gino.food.payment.service.domain.config.PaymentServiceConfigData;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.gino.food.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentCancelledKafkaMessagePublisher implements PaymentCancelledMessagePublisher {

  private final PaymentMessagingDataMapper paymentMessagingDataMapper;
  private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
  private final PaymentServiceConfigData paymentServiceConfigData;
  private final KafkaMessageHelper kafkaMessageHelper;

  @Override
  public void publish(PaymentCancelledEvent domainEvent) {
    String orderId = domainEvent.getPayment().getOrderId().getValue().toString();
    log.info("Received payment completed event for order id: {}", orderId);
    try {

      PaymentResponseAvroModel paymentResponseAvroModel =
          paymentMessagingDataMapper.paymentCancelledEventToPaymentResponseAvroModel(domainEvent);

      kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
          orderId,
          paymentResponseAvroModel,
          kafkaMessageHelper.getKafkaCallback(
              paymentServiceConfigData.getPaymentResponseTopicName(),
              paymentResponseAvroModel,
              orderId,
              "PaymentResponseAvroModel"));
      log.info("Sent payment response message to Kafka for order id: {}", orderId);
    } catch (Exception e) {
      log.error("Error while sending payment response message to Kafka for order id: {}", orderId,
          e);
    }

  }
}
