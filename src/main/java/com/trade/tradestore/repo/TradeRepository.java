package com.trade.tradestore.repo;

import com.trade.tradestore.domain.Trade;
import com.trade.tradestore.domain.TradeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, TradeKey> {
    @Query("select max(t.id.version) from Trade t where t.id.tradeId = :tradeId")
    Optional<Integer> findMaxVersion(@Param("tradeId") String tradeId);

    @Modifying
    @Query("update Trade t set t.expired = true where t.maturityDate < :today and t.expired = false")
    int markExpired(@Param("today") LocalDate today);
}
