package com.gino.food.payment.service.domain;

import com.food.gino.payment.service.domain.event.PaymentCancelledEvent;
import com.food.gino.payment.service.domain.event.PaymentCompletedEvent;
import com.food.gino.payment.service.domain.event.PaymentEvent;
import com.food.gino.payment.service.domain.event.PaymentFailedEvent;
import com.gino.food.payment.service.domain.dto.PaymentRequest;
import com.gino.food.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.gino.food.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

  private final PaymentRequestHelper paymentRequestHelper;

  @Override
  public void completePayment(PaymentRequest paymentRequest) {
    PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
    fireEvent(paymentEvent);
  }


  @Override
  public void cancelPayment(PaymentRequest paymentRequest) {
    PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
    fireEvent(paymentEvent);
  }

  private void fireEvent(PaymentEvent paymentEvent) {
    log.info("Publishing payment event for order id: {}", paymentEvent.getPayment().getOrderId());
    paymentEvent.fire();
  }
}
