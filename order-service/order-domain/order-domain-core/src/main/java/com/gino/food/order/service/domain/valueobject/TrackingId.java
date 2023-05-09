package com.gino.food.order.service.domain.valueobject;

import com.gino.food.domain.valueobject.BaseId;
import java.util.UUID;

public class TrackingId extends BaseId<UUID> {

  public TrackingId(UUID value) {
    super(value);
  }
}
