package com.gino.food.order.service.domain.exception;

import com.gino.food.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {

  public OrderNotFoundException(String message) {
    super(message);
  }
}
