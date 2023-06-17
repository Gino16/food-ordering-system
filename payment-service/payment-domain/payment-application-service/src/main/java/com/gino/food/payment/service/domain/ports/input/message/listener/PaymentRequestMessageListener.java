package com.gino.food.payment.service.domain.ports.input.message.listener;

import com.gino.food.payment.service.domain.dto.PaymentRequest;

public interface PaymentRequestMessageListener {

  void completePayment(PaymentRequest paymentRequest);

  void cancelPayment(PaymentRequest paymentRequest);

}
