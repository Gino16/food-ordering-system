package com.gino.food.order.service.domain.valueobject;

import com.gino.food.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {

  public OrderItemId(Long value) {
    super(value);
  }
}
