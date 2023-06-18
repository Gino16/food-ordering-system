package com.gino.food.restaurant.service.domain.exception;

import com.gino.food.domain.exception.DomainException;

public class RestaurantApplicationServiceException extends DomainException {

  public RestaurantApplicationServiceException(String message) {
    super(message);
  }

  public RestaurantApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
