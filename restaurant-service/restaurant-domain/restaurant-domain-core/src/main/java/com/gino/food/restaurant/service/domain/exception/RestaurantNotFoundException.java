package com.gino.food.restaurant.service.domain.exception;

import com.gino.food.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {

  public RestaurantNotFoundException(String message) {
    super(message);
  }
}
