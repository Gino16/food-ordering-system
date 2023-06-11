package com.food.gino.payment.service.domain.exception;

import com.gino.food.domain.exception.DomainException;

public class PaymentNotFoundException extends DomainException {

  public PaymentNotFoundException(String message) {
    super(message);
  }

  public PaymentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
