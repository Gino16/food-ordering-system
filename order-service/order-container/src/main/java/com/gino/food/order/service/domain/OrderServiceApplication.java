package com.gino.food.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.gino.food.order.service.dataaccess", "com.gino.food.dataaccess"})
@EntityScan(basePackages = {"com.gino.food.order.service.dataaccess", "com.gino.food.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.gino.food")
public class OrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceApplication.class, args);
  }
}
