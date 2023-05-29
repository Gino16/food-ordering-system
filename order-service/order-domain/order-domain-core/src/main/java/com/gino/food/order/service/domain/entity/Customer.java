package com.gino.food.order.service.domain.entity;

import com.gino.food.domain.entity.AggregateRoot;
import com.gino.food.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

  public Customer() {
  }

  public Customer(CustomerId customerId) {
    super.setId(customerId);
  }
}
