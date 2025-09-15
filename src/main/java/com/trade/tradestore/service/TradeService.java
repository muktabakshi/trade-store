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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class TradeService {
    @Autowired
    private  TradeRepository repo;
    @Autowired
    private  TradeAuditRepository auditRepo;
    @Autowired
    private TradeExpireAuditRepository expAuditRepo;
    @Autowired
    private  Clock clock;



    @Transactional
    public Trade upsert(TradeDto dto) throws TradeValidationException {
        LocalDate today = LocalDate.now(clock);
        TradeAuditDoc tradeAuditDoc = new TradeAuditDoc();
        BeanUtils.copyProperties(dto,tradeAuditDoc);
        auditRepo.save(tradeAuditDoc);
        if (dto.getMaturityDate() == null || dto.getMaturityDate().isBefore(today)) {
            tradeAuditDoc.setResponse("REJECTED_PAST_MATURITY");
            tradeAuditDoc.setRemarks("Maturity Date is before today");
            auditRepo.save(tradeAuditDoc);
            throw new TradeValidationException("Maturity date earlier than today");
        }

        int incomingVersion = dto.getVersion();
        Optional<Integer> maxOpt = repo.findMaxVersion(dto.getTradeId());
        if (maxOpt.isPresent() && incomingVersion < maxOpt.get()) {
            tradeAuditDoc.setResponse("REJECTED_LOWER_VERSION");
            tradeAuditDoc.setRemarks("incoming < existing");
            auditRepo.save(tradeAuditDoc);
            throw new TradeValidationException("Lower version received");
        }

        TradeKey key = new TradeKey(dto.getTradeId(), incomingVersion);
        Trade toSave = repo.findById(key).map(existing -> {
            existing.setBookId(dto.getBookId());
            existing.setCounterPartyId(dto.getCounterPartyId());
            existing.setMaturityDate(dto.getMaturityDate());
            return existing;
        }).orElseGet(() -> new Trade(key, dto.getCounterPartyId(), dto.getBookId(), dto.getMaturityDate()));

        Trade saved = repo.save(toSave);
        tradeAuditDoc.setResponse("Saved");
        tradeAuditDoc.setRemarks("Successfully saved");
        auditRepo.save(tradeAuditDoc);
        return saved;
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public int expirePastMaturity() {
        TradeExpireAuditDoc tradeExpireAuditDoc = new TradeExpireAuditDoc();
        tradeExpireAuditDoc.setMaturityDate(LocalDate.now(clock));
        LocalDate today = LocalDate.now(clock);
        expAuditRepo.save(tradeExpireAuditDoc);
        return repo.markExpired(today);
    }
}
