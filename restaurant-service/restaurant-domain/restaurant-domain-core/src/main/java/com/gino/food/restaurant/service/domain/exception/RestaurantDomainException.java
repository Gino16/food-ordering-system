package com.gino.food.restaurant.service.domain.exception;

import com.gino.food.domain.exception.DomainException;

public class RestaurantDomainException extends DomainException {

  public RestaurantDomainException(String message) {
    super(message);
  }

  public RestaurantDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
