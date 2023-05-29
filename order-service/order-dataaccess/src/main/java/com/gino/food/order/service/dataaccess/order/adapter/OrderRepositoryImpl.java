package com.gino.food.order.service.dataaccess.order.adapter;

import com.gino.food.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.gino.food.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.ports.output.repository.OrderRepository;
import com.gino.food.order.service.domain.valueobject.TrackingId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderJpaRepository orderJpaRepository;
  private final OrderDataAccessMapper orderDataAccessMapper;

  @Override
  public Order save(Order order) {
    return orderDataAccessMapper.orderEntityToOrder(
        orderJpaRepository.save(orderDataAccessMapper.orderToOrderEntity(order)));
  }

  @Override
  public Optional<Order> findByTrackingId(TrackingId trackingId) {
    return orderJpaRepository.findByTrackingId(trackingId.getValue())
        .map(orderDataAccessMapper::orderEntityToOrder);
  }
}
