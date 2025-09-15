package com.trade.tradestore.kafka;

import com.trade.tradestore.dto.TradeDto;
import com.trade.tradestore.exception.TradeValidationException;
import com.trade.tradestore.service.TradeService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class KafkaConsumerServiceTest  {

    private TradeService tradeService;
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        tradeService = mock(TradeService.class);
        kafkaConsumerService = new KafkaConsumerService(tradeService);
    }

    @Test
    void consume_shouldCallTradeServiceUpsert() throws TradeValidationException {
        // given
        TradeDto tradeDto = new TradeDto();
        ConsumerRecord<String, TradeDto> record = new ConsumerRecord<>("trades.incoming", 0, 0L, "key1", tradeDto);

        // when
        kafkaConsumerService.consume(record);

        // then
        ArgumentCaptor<TradeDto> captor = ArgumentCaptor.forClass(TradeDto.class);
        verify(tradeService, times(1)).upsert(captor.capture());

        assertThat(captor.getValue()).isEqualTo(tradeDto);
    }

    @Test
    void consume_shouldPropagateTradeValidationException() throws TradeValidationException {
        // given
        TradeDto tradeDto = new TradeDto();
        ConsumerRecord<String, TradeDto> record = new ConsumerRecord<>("trades.incoming", 0, 0L, "key1", tradeDto);

        doThrow(new TradeValidationException("Invalid trade"))
                .when(tradeService).upsert(any());

        // when + then
        try {
            kafkaConsumerService.consume(record);
        } catch (TradeValidationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Invalid trade");
        }
    }
}
