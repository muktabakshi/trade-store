package com.trade.tradestore.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trades", uniqueConstraints = @UniqueConstraint(columnNames = {"trade_id","version"}))
public class Trade {
    @EmbeddedId
    private TradeKey id;

    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private boolean expired;

    private Instant createdDate;
    private Instant updatedDate;

    public Trade(TradeKey id, String counterPartyId, String bookId, LocalDate maturityDate) {
        this.id = id;
        this.counterPartyId = counterPartyId;
        this.bookId = bookId;
        this.maturityDate = maturityDate;
        this.expired = false;
    }

    @PrePersist
    public void prePersist() { createdDate = Instant.now(); updatedDate = createdDate; }

    @PreUpdate
    public void preUpdate() { updatedDate = Instant.now(); }


}
