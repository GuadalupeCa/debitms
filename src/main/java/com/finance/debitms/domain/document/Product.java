package com.finance.debitms.domain.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
@Document(collection = "product")
public class Product{
    @Id
    private String id;
    private String name;
}
