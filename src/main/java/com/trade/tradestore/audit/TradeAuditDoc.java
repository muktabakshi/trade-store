package com.trade.tradestore.audit;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trade_audit")
public class TradeAuditDoc  {
    @Id
    private String id;
    private String tradeId;
    private int version;
    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private String response;
    private String remarks;
    @CreatedDate
    private Date createdDate = new Date();
    @LastModifiedDate
    private Date lastModifiedDate ;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;


}
