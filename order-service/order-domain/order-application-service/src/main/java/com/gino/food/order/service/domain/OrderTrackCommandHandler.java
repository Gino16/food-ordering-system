package com.gino.food.order.service.domain;

import com.gino.food.order.service.domain.dto.track.TrackOrderQuery;
import com.gino.food.order.service.domain.dto.track.TrackOrderResponse;
import com.gino.food.order.service.domain.entity.Order;
import com.gino.food.order.service.domain.exception.OrderNotFoundException;
import com.gino.food.order.service.domain.mapper.OrderDataMapper;
import com.gino.food.order.service.domain.ports.output.repository.OrderRepository;
import com.gino.food.order.service.domain.valueobject.TrackingId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderTrackCommandHandler {

  private final OrderDataMapper orderDataMapper;
  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
    Optional<Order> orderOptional = orderRepository.findByTrackingId(
        new TrackingId(trackOrderQuery.getOrderTrackingId()));
    if (orderOptional.isEmpty()) {
      log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
      throw new OrderNotFoundException(
          "Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());
    }
    return orderDataMapper.orderToTrackOrderResponse(orderOptional.get());
  }
}
