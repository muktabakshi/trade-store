package com.trade.tradestore.kafka;

import com.trade.tradestore.dto.TradeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, TradeDto> template;

    public KafkaProducerService(KafkaTemplate<String, TradeDto> template) {
        this.template = template;
    }

    public void publish(String topic, TradeDto dto) {
        template.send(topic, dto.getTradeId(), dto);
    }
}
