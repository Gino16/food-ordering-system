package com.gino.food.restaurant.service.domain.valueobject;

import com.gino.food.domain.valueobject.BaseId;
import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {

  public OrderApprovalId(UUID value) {
    super(value);
  }
}
