package com.food.gino.payment.service.domain;

import com.food.gino.payment.service.domain.entity.CreditEntry;
import com.food.gino.payment.service.domain.entity.CreditHistory;
import com.food.gino.payment.service.domain.entity.Payment;
import com.food.gino.payment.service.domain.event.PaymentEvent;
import java.util.List;

public interface PaymentDomainService {

  PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages);

  PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
      List<CreditHistory> creditHistories, List<String> failureMessages);
}
