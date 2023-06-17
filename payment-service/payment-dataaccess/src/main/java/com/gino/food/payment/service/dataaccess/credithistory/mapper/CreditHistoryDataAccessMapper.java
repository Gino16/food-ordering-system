package com.gino.food.payment.service.dataaccess.credithistory.mapper;


import com.food.gino.payment.service.domain.entity.CreditHistory;
import com.food.gino.payment.service.domain.valueobject.CreditHistoryId;
import com.gino.food.domain.valueobject.CustomerId;
import com.gino.food.domain.valueobject.Money;
import com.gino.food.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryDataAccessMapper {

  public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
    return CreditHistory.Builder.builder()
        .creditHistoryId(new CreditHistoryId(creditHistoryEntity.getId()))
        .customerId(new CustomerId(creditHistoryEntity.getCustomerId()))
        .amount(new Money(creditHistoryEntity.getAmount()))
        .transactionType(creditHistoryEntity.getType())
        .build();
  }

  public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
    return CreditHistoryEntity.builder()
        .id(creditHistory.getId().getValue())
        .customerId(creditHistory.getCustomerId().getValue())
        .amount(creditHistory.getAmount().getAmount())
        .type(creditHistory.getTransactionType())
        .build();
  }

}
