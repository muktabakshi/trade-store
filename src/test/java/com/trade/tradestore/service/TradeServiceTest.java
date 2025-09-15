package com.trade.tradestore.service;

import com.trade.tradestore.audit.TradeAuditDoc;
import com.trade.tradestore.audit.TradeAuditRepository;
import com.trade.tradestore.audit.TradeExpireAuditDoc;
import com.trade.tradestore.audit.TradeExpireAuditRepository;
import com.trade.tradestore.domain.Trade;
import com.trade.tradestore.domain.TradeKey;
import com.trade.tradestore.dto.TradeDto;
import com.trade.tradestore.exception.TradeValidationException;
import com.trade.tradestore.repo.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    @Mock
    private TradeRepository repo;
    @Mock
    private TradeAuditRepository auditRepo;
    @Mock
    private TradeExpireAuditRepository expAuditRepo;
    @Mock
    private Clock clock;

    @InjectMocks
    private TradeService service;

    private final LocalDate fixedDate = LocalDate.of(2025, 9, 15);
    private final Clock fixedClock = Clock.fixed(fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    void testUpsert_SuccessfulSave_NewTrade() throws Exception {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");
        dto.setVersion(1);
        dto.setBookId("B1");
        dto.setCounterPartyId("C1");
        dto.setMaturityDate(fixedDate.plusDays(1));

        when(repo.findMaxVersion("T1")).thenReturn(Optional.empty());
        when(repo.findById(any())).thenReturn(Optional.empty());
        when(repo.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trade result = service.upsert(dto);

        assertNotNull(result);
        assertEquals("B1", result.getBookId());
        verify(auditRepo, atLeastOnce()).save(any(TradeAuditDoc.class));
        verify(repo).save(any(Trade.class));
    }

    @Test
    void testUpsert_RejectsPastMaturity() {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");
        dto.setVersion(1);
        dto.setMaturityDate(fixedDate.minusDays(1));

        assertThrows(TradeValidationException.class, () -> service.upsert(dto));
        verify(auditRepo, atLeastOnce()).save(any(TradeAuditDoc.class));
        verify(repo, never()).save(any());
    }

    @Test
    void testUpsert_RejectsLowerVersion() {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");
        dto.setVersion(1);
        dto.setMaturityDate(fixedDate.plusDays(1));

        when(repo.findMaxVersion("T1")).thenReturn(Optional.of(2));

        assertThrows(TradeValidationException.class, () -> service.upsert(dto));
        verify(auditRepo, atLeastOnce()).save(any(TradeAuditDoc.class));
        verify(repo, never()).save(any());
    }

    @Test
    void testUpsert_UpdatesExistingTrade() throws Exception {
        TradeDto dto = new TradeDto();
        dto.setTradeId("T1");
        dto.setVersion(2);
        dto.setBookId("B2");
        dto.setCounterPartyId("C2");
        dto.setMaturityDate(fixedDate.plusDays(5));

        Trade existing = new Trade(new TradeKey("T1", 2), "C1", "B1", fixedDate.plusDays(2));

        when(repo.findMaxVersion("T1")).thenReturn(Optional.of(2));
        when(repo.findById(any())).thenReturn(Optional.of(existing));
        when(repo.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trade result = service.upsert(dto);

        assertEquals("B2", result.getBookId());
        assertEquals("C2", result.getCounterPartyId());
        verify(repo).save(existing);
        verify(auditRepo, atLeastOnce()).save(any(TradeAuditDoc.class));
    }

    @Test
    void testExpirePastMaturity() {
        when(repo.markExpired(fixedDate)).thenReturn(3);

        int expiredCount = service.expirePastMaturity();

        assertEquals(3, expiredCount);
        verify(expAuditRepo).save(any(TradeExpireAuditDoc.class));
        verify(repo).markExpired(fixedDate);
    }
}
