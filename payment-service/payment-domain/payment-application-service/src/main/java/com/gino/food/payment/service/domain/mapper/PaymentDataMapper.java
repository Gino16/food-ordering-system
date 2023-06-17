package com.gino.food.payment.service.domain.mapper;

import com.food.gino.payment.service.domain.entity.Payment;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.OrderId;
import com.gino.food.payment.service.domain.dto.PaymentRequest;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataMapper {

  public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
    return Payment.Builder.builder()
        .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
        .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
        .price(new Money(paymentRequest.getPrice()))
        .build();
  }
}
