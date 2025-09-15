package com.trade.tradestore.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeExpireAuditRepository extends MongoRepository<TradeExpireAuditDoc, String> {}
