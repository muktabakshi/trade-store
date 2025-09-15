package com.trade.tradestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.tradestore.domain.Trade;
import com.trade.tradestore.domain.TradeKey;
import com.trade.tradestore.dto.TradeDto;
import com.trade.tradestore.exception.TradeValidationException;
import com.trade.tradestore.kafka.KafkaProducerService;
import com.trade.tradestore.service.TradeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void upsert_ShouldReturnTrade() throws Exception {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");
        dto.setVersion(1);

        Trade savedTrade = new Trade();
        TradeKey tradeKey = new TradeKey("T1",1);
        savedTrade.setId(tradeKey);
        when(tradeService.upsert(any(TradeDto.class))).thenReturn(savedTrade);

        mockMvc.perform(post("/api/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.tradeId").value("T1"))
                .andExpect(jsonPath("$.id.version").value(1));

        verify(tradeService, times(1)).upsert(any(TradeDto.class));
    }

    @Test
    void upsert_ShouldReturnBadRequest_OnValidationException() throws Exception {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");

        when(tradeService.upsert(any(TradeDto.class)))
                .thenThrow(new TradeValidationException("Invalid trade"));

        mockMvc.perform(post("/api/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        verify(tradeService, times(1)).upsert(any(TradeDto.class));
    }

    @Test
    void expire_ShouldReturnCount() throws Exception {
        when(tradeService.expirePastMaturity()).thenReturn(5);

        mockMvc.perform(post("/api/trades/expire"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(tradeService, times(1)).expirePastMaturity();
    }

    @Test
    void publish_ShouldReturnMessage() throws Exception {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");

        doNothing().when(kafkaProducerService).publish(anyString(), any(TradeDto.class));

        mockMvc.perform(post("/api/trades/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message published"));

        verify(kafkaProducerService, times(1)).publish(anyString(), any(TradeDto.class));
    }
}
