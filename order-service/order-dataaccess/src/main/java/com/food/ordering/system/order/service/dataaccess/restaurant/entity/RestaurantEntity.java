package com.food.ordering.system.order.service.dataaccess.restaurant.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_restaurant_m_view", schema = "restaurant")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RestaurantEntityId.class)
public class RestaurantEntity {

  @Id
  private UUID id;
  @Id
  private UUID productId;

  private String restaurantName;
  private Boolean restaurantActive;
  private String productName;
  private BigDecimal productPrice;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestaurantEntity that = (RestaurantEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(productId,
        that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId);
  }
}
