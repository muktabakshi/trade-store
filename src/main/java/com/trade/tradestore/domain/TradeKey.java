package com.trade.tradestore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class TradeKey implements Serializable {
    @Column(name = "trade_id", nullable = false)
    private String tradeId;

    @Column(name = "version", nullable = false)
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeKey)) return false;
        TradeKey tradeKey = (TradeKey) o;
        return version == tradeKey.version && Objects.equals(tradeId, tradeKey.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, version);
    }
}
