package com.food.ordering.system.order.service.domain;

import static com.food.ordering.system.domain.DomainConstants.FAILURE_MESSAGE_DELIMITER;

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

  private final OrderPaymentSaga orderPaymentSaga;

  @Override
  public void paymentCompleted(PaymentResponse paymentResponse) {
    orderPaymentSaga.process(paymentResponse);
    log.info("OrderPaymentSaga process for order id: {}", paymentResponse.getOrderId());
  }

  @Override
  public void paymentCancelled(PaymentResponse paymentResponse) {

    orderPaymentSaga.rollback(paymentResponse);
    log.info("Order is roll backed with order id: {} and failure message: {}",
        paymentResponse.getOrderId(),
        String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessages()));
  }
}
