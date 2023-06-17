package com.gino.food.payment.service.domain.ports.output.repository;

import com.food.gino.payment.service.domain.entity.CreditEntry;
import com.gino.food.domain.valueobject.CustomerId;
import java.util.Optional;

public interface CreditEntryRepository {

  CreditEntry save(CreditEntry creditEntry);

  Optional<CreditEntry> findByCustomerId(CustomerId customerId);

}
