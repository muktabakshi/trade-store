package com.trade.tradestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.trade.tradestore.repo")
public class TradeStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeStoreApplication.class, args);
    }
}
