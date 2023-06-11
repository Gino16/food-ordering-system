package com.food.gino.payment.service.domain.entity;

import com.food.gino.payment.service.domain.valueobject.CreditEntryId;
import com.gino.food.domain.entity.BaseEntity;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;

public class CreditEntry extends BaseEntity<CreditEntryId> {

  private final CustomerId customerId;
  private Money totalCreditAmount;

  public void addCreditAmount(Money amount) {
    totalCreditAmount = totalCreditAmount.add(amount);
  }

  public void subtractCreditAmount(Money amount) {
    totalCreditAmount = totalCreditAmount.subtract(amount);
  }

  private CreditEntry(Builder builder) {
    setId(builder.creditEntryId);
    customerId = builder.customerId;
    totalCreditAmount = builder.totalCreditAmount;
  }

  public CustomerId getCustomerId() {
    return customerId;
  }

  public Money getTotalCreditAmount() {
    return totalCreditAmount;
  }


  public static final class Builder {

    private CreditEntryId creditEntryId;
    private CustomerId customerId;
    private Money totalCreditAmount;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder id(CreditEntryId val) {
      creditEntryId = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder totalCreditAmount(Money val) {
      totalCreditAmount = val;
      return this;
    }

    public CreditEntry build() {
      return new CreditEntry(this);
    }
  }
}
