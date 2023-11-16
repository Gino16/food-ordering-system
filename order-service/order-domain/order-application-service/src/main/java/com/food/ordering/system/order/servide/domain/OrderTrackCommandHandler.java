package com.food.ordering.system.order.servide.domain;

import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import com.food.ordering.system.order.servide.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.servide.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.servide.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.servide.domain.ports.output.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

  private final OrderDataMapper orderDataMapper;
  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
    var orderResult =
        orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
    if (orderResult.isEmpty()) {
      log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
      throw new OrderNotFoundException(
          "Could not find order with tracking id:" + trackOrderQuery.getOrderTrackingId());
    }
    return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
  }
}
