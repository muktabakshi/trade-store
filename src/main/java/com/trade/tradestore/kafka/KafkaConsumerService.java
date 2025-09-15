package com.trade.tradestore.kafka;

import com.trade.tradestore.dto.TradeDto;
import com.trade.tradestore.exception.TradeValidationException;
import com.trade.tradestore.service.TradeService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final TradeService service;

    public KafkaConsumerService(TradeService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${kafka.topic:trades.incoming}", groupId = "trade-store")
    public void consume(ConsumerRecord<String, TradeDto> record) throws TradeValidationException {
        service.upsert(record.value());
    }
}
