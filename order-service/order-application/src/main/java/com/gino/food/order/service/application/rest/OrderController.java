package com.gino.food.order.service.application.rest;

import com.gino.food.order.service.domain.dto.create.CreateOrderCommand;
import com.gino.food.order.service.domain.dto.create.CreateOrderResponse;
import com.gino.food.order.service.domain.dto.track.TrackOrderQuery;
import com.gino.food.order.service.domain.dto.track.TrackOrderResponse;
import com.gino.food.order.service.domain.ports.input.service.OrderApplicationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

  private final OrderApplicationService orderApplicationService;

  @PostMapping
  public ResponseEntity<CreateOrderResponse> createOrder(
      @RequestBody CreateOrderCommand createOrderCommand) {
    log.info("Creating order for customer: {} at restaurant: {}",
        createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
    return ResponseEntity.ok(orderApplicationService.createOrder(createOrderCommand));
  }

  @GetMapping("/{trackingId}")
  public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
    return ResponseEntity.ok(orderApplicationService.trackOrder(
        TrackOrderQuery.builder().orderTrackingId(trackingId).build()));
  }
}
