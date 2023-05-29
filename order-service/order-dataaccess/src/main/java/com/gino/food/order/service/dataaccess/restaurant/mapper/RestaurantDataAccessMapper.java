package com.gino.food.order.service.dataaccess.restaurant.mapper;

import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.ProductId;
import com.gino.food.domain.valueobject.RestaurantId;
import com.gino.food.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.gino.food.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.gino.food.order.service.domain.entity.Product;
import com.gino.food.order.service.domain.entity.Restaurant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataAccessMapper {

  public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
    return restaurant.getProducts().stream()
        .map(product -> product.getId().getValue())
        .collect(Collectors.toList());
  }

  public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
    RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst()
        .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));
    List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
        new Product(new ProductId(entity.getProductId()), entity.getProductName(), new Money(entity.getProductPrice())))
        .toList();
    return Restaurant.Builder
        .builder()
        .id(new RestaurantId(restaurantEntity.getProductId()))
        .products(restaurantProducts)
        .active(restaurantEntity.getRestaurantActive())
        .build();
  }
}
