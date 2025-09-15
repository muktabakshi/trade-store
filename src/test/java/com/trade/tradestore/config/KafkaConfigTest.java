package com.trade.tradestore.config;

import com.trade.tradestore.dto.TradeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = KafkaConfig.class)
class KafkaConfigTest {

    @Autowired
    private ProducerFactory<String, TradeDto> producerFactory;

    @Autowired
    private KafkaTemplate<String, TradeDto> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String, TradeDto> consumerFactory;

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, TradeDto> kafkaListenerContainerFactory;

    @Test
    void producerFactoryBeanShouldLoad() {
        assertThat(producerFactory).isNotNull();
    }

    @Test
    void kafkaTemplateBeanShouldLoad() {
        assertThat(kafkaTemplate).isNotNull();
    }

    @Test
    void consumerFactoryBeanShouldLoad() {
        assertThat(consumerFactory).isNotNull();
    }

    @Test
    void kafkaListenerContainerFactoryBeanShouldLoad() {
        assertThat(kafkaListenerContainerFactory).isNotNull();
    }
}
