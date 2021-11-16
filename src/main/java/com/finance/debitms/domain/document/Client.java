package com.finance.debitms.domain.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    @Id
    private String id;
    private String documentIdentityType;
    @NotNull(message = "Es necesario el numero del docuemtno de identidad")
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
