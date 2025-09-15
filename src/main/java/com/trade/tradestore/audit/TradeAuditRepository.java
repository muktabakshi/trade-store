package com.trade.tradestore.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeAuditRepository extends MongoRepository<TradeAuditDoc, String> {}
