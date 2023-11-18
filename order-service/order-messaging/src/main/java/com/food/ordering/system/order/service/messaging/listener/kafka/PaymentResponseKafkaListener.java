package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.order.servide.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

  private final PaymentResponseMessageListener paymentResponseMessageListener;
  private final OrderMessagingDataMapper orderMessagingDataMapper;

  @Override
  @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order"
      + "-service.payment-response-topic-name}")
  public void receive(@Payload List<PaymentResponseAvroModel> messages,
      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
      @Header(KafkaHeaders.OFFSET) List<Long> offset) {
    log.info("{} number of payment repsonses received with keys: {}, partitions: {} and offsets: "
        + "{}", messages.size(), keys.toString(), partitions.toString(), offset.toString());

    messages.forEach(paymentResponseAvroModel -> {
      if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
        log.info("Processing successful payment order id: {}",
            paymentResponseAvroModel.getOrderId());
        paymentResponseMessageListener.paymentCompleted(
            orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                paymentResponseAvroModel));
      } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus()
          || PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()) {
        log.info("Processing unsuccessful payment order id: {}",
            paymentResponseAvroModel.getOrderId());
        paymentResponseMessageListener.paymentCancelled(
            orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                paymentResponseAvroModel));
      }
    });
  }
}
