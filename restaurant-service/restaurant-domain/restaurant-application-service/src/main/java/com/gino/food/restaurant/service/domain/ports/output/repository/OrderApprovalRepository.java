package com.gino.food.restaurant.service.domain.ports.output.repository;

import com.gino.food.restaurant.service.domain.entity.OrderApproval;

public interface OrderApprovalRepository {

  OrderApproval save(OrderApproval orderApproval);

}
