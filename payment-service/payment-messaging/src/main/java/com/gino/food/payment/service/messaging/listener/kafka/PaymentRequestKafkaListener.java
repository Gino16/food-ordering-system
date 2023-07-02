package com.gino.food.payment.service.messaging.listener.kafka;

import com.gino.food.kafka.consumer.KafkaConsumer;
import com.gino.food.kafka.order.avro.model.PaymentOrderStatus;
import com.gino.food.kafka.order.avro.model.PaymentRequestAvroModel;
import com.gino.food.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.gino.food.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

  private final PaymentRequestMessageListener paymentRequestMessageListener;
  private final PaymentMessagingDataMapper paymentMessagingDataMapper;

  @Override
  @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
      topics = "${payment-service.payment-request-topic-name}")
  public void receive(
      @Payload List<PaymentRequestAvroModel> messages,
      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
    log.info("Received {} messages", messages.size());
    messages.forEach(message -> {
      if (PaymentOrderStatus.PENDING == message.getPaymentOrderStatus()) {
        paymentRequestMessageListener
            .completePayment(
                paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(message));
      } else if (PaymentOrderStatus.CANCELLED == message.getPaymentOrderStatus()) {
        paymentRequestMessageListener
            .cancelPayment(
                paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(message));
      }
    });
  }
}
