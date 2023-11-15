package com.food.ordering.system.order.servide.domain.ports.input.message.listener.payment;

import com.food.ordering.system.order.servide.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

  void paymentCompleted(PaymentResponse paymentResponse);

  void paymentCancelled(PaymentResponse paymentResponse);
}
