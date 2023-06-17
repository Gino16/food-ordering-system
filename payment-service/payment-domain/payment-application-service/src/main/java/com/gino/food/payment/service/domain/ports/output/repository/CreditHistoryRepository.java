package com.gino.food.payment.service.domain.ports.output.repository;

import com.food.gino.payment.service.domain.entity.CreditHistory;
import com.gino.food.domain.valueobject.CustomerId;
import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {

  CreditHistory save(CreditHistory creditHistory);

  Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);

}
