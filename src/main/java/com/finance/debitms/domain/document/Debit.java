package com.finance.debitms.domain.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@Data
@Document(collection = "debit")
public class Debit {
    @Id
    private String id;
    private Client client;
    private Product product;
    private double balance;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
}
