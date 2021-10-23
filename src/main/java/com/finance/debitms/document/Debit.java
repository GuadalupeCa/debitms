package com.finance.debitms.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Data
@Document(collection = "debit")
public class Debit {
    @Id
    private String id;
    private String productId;
    private String clientId;
    private String account;
}
