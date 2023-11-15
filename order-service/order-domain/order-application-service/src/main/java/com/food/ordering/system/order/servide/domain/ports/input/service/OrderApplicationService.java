package com.food.ordering.system.order.servide.domain.ports.input.service;

import com.food.ordering.system.order.servide.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.servide.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.servide.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.servide.domain.dto.track.TrackOrderResponse;
import javax.validation.Valid;

public interface OrderApplicationService {

  CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

  TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
