package com.finance.debitms.domain.document;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Client {

    private String documentIdentityType;
    private String documentIdentityNumber;
    private String name;
    private String type;
    private String phoneNumber;
    private String address;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;


}
