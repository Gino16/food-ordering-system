package com.gino.food.payment.service.domain.exception;

import com.gino.food.domain.exception.DomainException;

public class PaymentApplicationServiceException extends DomainException {

  public PaymentApplicationServiceException(String message) {
    super(message);
  }

  public PaymentApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
