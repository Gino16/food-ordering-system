package com.gino.food.payment.service.dataaccess.creditentry.mapper;


import com.food.gino.payment.service.domain.entity.CreditEntry;
import com.food.gino.payment.service.domain.valueobject.CreditEntryId;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

  public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
    return CreditEntry.Builder.builder()
        .id(new CreditEntryId(creditEntryEntity.getId()))
        .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
        .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
        .build();
  }

  public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
    return CreditEntryEntity.builder()
        .id(creditEntry.getId().getValue())
        .customerId(creditEntry.getCustomerId().getValue())
        .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
        .build();
  }

}
