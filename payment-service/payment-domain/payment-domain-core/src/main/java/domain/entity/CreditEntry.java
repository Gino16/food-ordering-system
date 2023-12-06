package domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import domain.valueobject.CreditEntryId;

public class CreditEntry extends BaseEntity<CreditEntryId> {

  private final CustomerId customerId;
  private Money totalCreditAmount;

  public void addCreditAmount(Money amount) {
    totalCreditAmount = totalCreditAmount.add(amount);
  }

  public void subtractCreditAmount(Money amount) {
    totalCreditAmount = totalCreditAmount.subtract(amount);
  }

  public static Builder builder() {
    return new Builder();
  }

  private CreditEntry(Builder builder) {
    super.setId(builder.creditEntryId);
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


    public Builder id(CreditEntryId creditEntryId) {
      this.creditEntryId = creditEntryId;
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
