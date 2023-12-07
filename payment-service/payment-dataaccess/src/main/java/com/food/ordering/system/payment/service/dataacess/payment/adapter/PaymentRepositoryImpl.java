package com.food.ordering.system.payment.service.dataacess.payment.adapter;

import com.food.ordering.system.payment.service.dataacess.payment.mapper.PaymentDataAccessMapper;
import com.food.ordering.system.payment.service.dataacess.payment.repository.PaymentJpaRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import domain.entity.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

  private final PaymentJpaRepository paymentJpaRepository;
  private final PaymentDataAccessMapper paymentDataAccessMapper;

  public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository,
      PaymentDataAccessMapper paymentDataAccessMapper) {
    this.paymentJpaRepository = paymentJpaRepository;
    this.paymentDataAccessMapper = paymentDataAccessMapper;
  }

  @Override
  public Payment save(Payment payment) {
    return paymentDataAccessMapper
        .paymentEntityToPayment(paymentJpaRepository
            .save(paymentDataAccessMapper.paymentToPaymentEntity(payment)));
  }

  @Override
  public Optional<Payment> findByOrderId(UUID orderId) {
    return paymentJpaRepository.findByOrderId(orderId)
        .map(paymentDataAccessMapper::paymentEntityToPayment);
  }
}
