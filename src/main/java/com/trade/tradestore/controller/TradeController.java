package com.trade.tradestore.controller;

import com.trade.tradestore.domain.Trade;
import com.trade.tradestore.dto.TradeDto;
import com.trade.tradestore.exception.TradeValidationException;
import com.trade.tradestore.kafka.KafkaProducerService;
import com.trade.tradestore.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trades")
public class TradeController {
    @Autowired
    private  TradeService service;

    @Autowired
    private KafkaProducerService kafkaProducerService;
    //public TradeController(TradeService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Trade> upsert(@Valid @RequestBody TradeDto dto) throws TradeValidationException {

            return ResponseEntity.ok(service.upsert(dto));

    }

    @PostMapping("/expire")
    public ResponseEntity<Integer> expire() {
        return ResponseEntity.ok(service.expirePastMaturity());
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@Valid @RequestBody TradeDto dto) throws TradeValidationException {

        kafkaProducerService.publish("trades.incoming",dto);

        return ResponseEntity.ok("Message published");
    }
}
