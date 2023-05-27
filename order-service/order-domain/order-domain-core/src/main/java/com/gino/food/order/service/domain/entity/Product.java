package com.gino.food.order.service.domain.entity;

import com.gino.food.domain.entity.BaseEntity;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {

  private String name;
  private Money price;

  public Product(ProductId productId) {
    super.setId(productId);
  }

  public Product(ProductId productId, String name, Money price) {
    super.setId(productId);
    this.name = name;
    this.price = price;
  }

  public void updateWithConfirmedNameAndPrice(String name, Money price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public Money getPrice() {
    return price;
  }
}
