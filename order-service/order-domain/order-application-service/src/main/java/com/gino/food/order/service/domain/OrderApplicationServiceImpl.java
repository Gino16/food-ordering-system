package com.gino.food.order.service.domain;

import com.gino.food.order.service.domain.dto.create.CreateOrderCommand;
import com.gino.food.order.service.domain.dto.create.CreateOrderResponse;
import com.gino.food.order.service.domain.dto.track.TrackOrderQuery;
import com.gino.food.order.service.domain.dto.track.TrackOrderResponse;
import com.gino.food.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

  private final OrderCreateCommandHandler orderCreateCommandHandler;
  private final OrderTrackCommandHandler orderTrackCommandHandler;

  @Override
  public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
    return orderCreateCommandHandler.createOrderResponse(createOrderCommand);
  }

  @Override
  public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
    return orderTrackCommandHandler.trackOrder(trackOrderQuery);
  }
}
