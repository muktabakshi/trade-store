package com.trade.tradestore.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trade_expire")
public class TradeExpireAuditDoc {
    @Id
    private String id;
    private LocalDate maturityDate;
    @CreatedDate
    private Date createdDate = new Date();
    @LastModifiedDate
    private Date lastModifiedDate ;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;


}
