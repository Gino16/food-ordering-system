package com.gino.food.payment.service.dataaccess.payment.mapper;


import com.food.gino.payment.service.domain.entity.Payment;
import com.food.gino.payment.service.domain.valueobject.PaymentId;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.OrderId;
import com.gino.food.payment.service.dataaccess.payment.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataAccessMapper {

  public PaymentEntity paymentToPaymentEntity(Payment payment) {
    return PaymentEntity.builder()
        .id(payment.getId().getValue())
        .customerId(payment.getCustomerId().getValue())
        .orderId(payment.getOrderId().getValue())
        .price(payment.getPrice().getAmount())
        .status(payment.getPaymentStatus())
        .createdAt(payment.getCreatedAt())
        .build();
  }

  public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
    return Payment.Builder.builder()
        .paymentId(new PaymentId(paymentEntity.getId()))
        .customerId(new CustomerId(paymentEntity.getCustomerId()))
        .orderId(new OrderId(paymentEntity.getOrderId()))
        .price(new Money(paymentEntity.getPrice()))
        .createdAt(paymentEntity.getCreatedAt())
        .build();
  }

}
