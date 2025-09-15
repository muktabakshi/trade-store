package com.trade.tradestore.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Clock;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TimeConfig.class)
class TimeConfigTest {

    @Autowired
    private Clock clock;

    @Test
    void clockBeanShouldNotBeNull() {
        assertThat(clock).isNotNull();
    }

    @Test
    void defaultTimezoneShouldBeUTC() {
        ZoneId zone = clock.getZone();
        assertThat(zone).isEqualTo(ZoneId.of("Asia/Kolkata"));
    }
}
