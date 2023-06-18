package com.gino.food.payment.service.domain;

import com.food.gino.payment.service.domain.PaymentDomainService;
import com.food.gino.payment.service.domain.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  public PaymentDomainService paymentDomainService() {
    return new PaymentDomainServiceImpl();
  }
}
