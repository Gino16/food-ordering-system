package com.food.ordering.system.order.service.dataaccess.order.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderItemEntityId.class)
public class OrderItemEntity {

  @Id
  private Long id;

  @Id
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "ORDER_ID")
  private OrderEntity order;

  private UUID productId;
  private BigDecimal price;
  private Integer quantity;
  private BigDecimal subTotal;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItemEntity that = (OrderItemEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(order, that.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, order);
  }
}
