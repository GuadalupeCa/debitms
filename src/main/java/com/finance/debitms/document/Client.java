package com.finance.debitms.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "clients")
@Getter
@Setter
@ToString
public class Client {
    @Id
    private String id;
    private String DNI;
    private String name;
    private String direction;
    private String phoneNumber;


    public Client(String DNI, String name, String direction, String phoneNumber) {
        this.DNI = DNI;
        this.name = name;
        this.direction = direction;
        this.phoneNumber = phoneNumber;

    }
}
