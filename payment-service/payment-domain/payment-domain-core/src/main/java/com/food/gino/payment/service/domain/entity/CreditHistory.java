package com.food.gino.payment.service.domain.entity;

import com.food.gino.payment.service.domain.valueobject.CreditHistoryId;
import com.food.gino.payment.service.domain.valueobject.TransactionType;
import com.gino.food.domain.entity.BaseEntity;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;

public class CreditHistory extends BaseEntity<CreditHistoryId> {

  private final CustomerId customerId;
  private final Money amount;
  private final TransactionType transactionType;

  private CreditHistory(Builder builder) {
    setId(builder.creditHistoryId);
    customerId = builder.customerId;
    amount = builder.amount;
    transactionType = builder.transactionType;
  }

  public CustomerId getCustomerId() {
    return customerId;
  }

  public Money getAmount() {
    return amount;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }


  public static final class Builder {

    private CreditHistoryId creditHistoryId;
    private CustomerId customerId;
    private Money amount;
    private TransactionType transactionType;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder creditHistoryId(CreditHistoryId val) {
      creditHistoryId = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder amount(Money val) {
      amount = val;
      return this;
    }

    public Builder transactionType(TransactionType val) {
      transactionType = val;
      return this;
    }

    public CreditHistory build() {
      return new CreditHistory(this);
    }
  }
}
