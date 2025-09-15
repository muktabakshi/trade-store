package com.trade.tradestore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock(@Value("${tradestore.timezone:UTC}") String tz) {
        return Clock.system(ZoneId.of(tz));
    }
}
