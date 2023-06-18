package com.gino.food.restaurant.service.dataaccess.restaurant.mapper;


import com.gino.food.dataaccess.restaurant.entity.RestaurantEntity;
import com.gino.food.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.OrderId;
import com.gino.food.domain.valueobject.ProductId;
import com.gino.food.domain.valueobject.RestaurantId;
import com.gino.food.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import com.gino.food.restaurant.service.domain.entity.OrderApproval;
import com.gino.food.restaurant.service.domain.entity.OrderDetail;
import com.gino.food.restaurant.service.domain.entity.Product;
import com.gino.food.restaurant.service.domain.entity.Restaurant;
import com.gino.food.restaurant.service.domain.valueobject.OrderApprovalId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataAccessMapper {

  public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
    return restaurant.getOrderDetail().getProducts().stream()
        .map(product -> product.getId().getValue())
        .collect(Collectors.toList());
  }

  public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
    RestaurantEntity restaurantEntity =
        restaurantEntities.stream().findFirst().orElseThrow(() ->
            new RestaurantDataAccessException("No restaurants found!"));

    List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
            Product.Builder.builder()
                .id(new ProductId(entity.getProductId()))
                .name(entity.getProductName())
                .price(new Money(entity.getProductPrice()))
                .available(entity.getProductAvailable())
                .build())
        .collect(Collectors.toList());

    return Restaurant.Builder.builder()
        .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
        .orderDetail(OrderDetail.Builder.builder()
            .products(restaurantProducts)
            .build())
        .active(restaurantEntity.getRestaurantActive())
        .build();
  }

  public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
    return OrderApprovalEntity.builder()
        .id(orderApproval.getId().getValue())
        .restaurantId(orderApproval.getRestaurantId().getValue())
        .orderId(orderApproval.getOrderId().getValue())
        .status(orderApproval.getApprovalStatus())
        .build();
  }

  public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
    return OrderApproval.Builder.builder()
        .id(new OrderApprovalId(orderApprovalEntity.getId()))
        .restaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
        .orderId(new OrderId(orderApprovalEntity.getOrderId()))
        .approvalStatus(orderApprovalEntity.getStatus())
        .build();
  }

}
