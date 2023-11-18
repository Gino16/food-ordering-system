package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.servide.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.servide.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.servide.domain.ports.input.service.OrderApplicationService;
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
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
@RequiredArgsConstructor
public class OrderController {

  private final OrderApplicationService orderApplicationService;

  @PostMapping
  public ResponseEntity<?> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
    log.info("Creating order for customer: {} at restaurant: {}",
        createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
    var createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
    log.info("Order created with tracking id: {}", createOrderResponse.getOrderTrackingId());
    return ResponseEntity.ok(createOrderResponse);
  }

  @GetMapping("/{trackingId}")
  public ResponseEntity<?> getOrderByTrackingId(@PathVariable UUID trackingId) {
    var trackOrderResponse = orderApplicationService.trackOrder(
        TrackOrderQuery.builder().orderTrackingId(trackingId).build());
    log.info("Returning order status with tracking id: {}",
        trackOrderResponse.getOrderTrackingId());
    return ResponseEntity.ok(trackOrderResponse);
  }
}
