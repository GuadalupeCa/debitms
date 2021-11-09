package com.finance.debitms.domain.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Client {

    @Id
    private String id;
    private String documentIdentityType;
    private String documentIdentityNumber;
    private String name;
    private Type type;
    private String gender;
    private String phoneNumber;
    private String address;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;


}
