package com.gino.food.payment.service.domain;

import com.gino.food.payment.service.domain.dto.PaymentRequest;
import com.gino.food.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

  @Override
  public void completePayment(PaymentRequest paymentRequest) {

  }

  @Override
  public void cancelPayment(PaymentRequest paymentRequest) {

  }
}
