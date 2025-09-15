package com.trade.tradestore.kafka;

import com.trade.tradestore.dto.TradeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, TradeDto> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublish_Success() {
        // Arrange
        TradeDto tradeDto = new TradeDto();
        tradeDto.setTradeId("T1");
        tradeDto.setVersion(1);
        tradeDto.setCounterPartyId("CP-1");
        tradeDto.setBookId("B1");

        String topic = "trades.incoming";

        // Act
        kafkaProducerService.publish(topic, tradeDto);

        // Assert
        verify(kafkaTemplate, times(1))
                .send(topic, tradeDto.getTradeId(), tradeDto);
    }
}
