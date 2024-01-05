package exception;

import com.food.ordering.system.domain.exception.DomainException;

public class CustomerDomainException extends DomainException {

  public CustomerDomainException(String message) {
    super(message);
  }
}
