package com.finance.debitms.domain.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "debit")
public class Debit {
    @Id
    private String id;
    @NotNull(message = "Es necesario el cliente")
    private Client client;
    @NotNull(message = "Es necesario al menos un representante")
    private List<Client> representatives;
    private List<Client> signers;
    @NotNull(message = "Es necesario el número de cuenta")
    private String account;
    @NotNull(message = "Es necesario el producto de la cuenta de debito")
    private Product product;
    private double balance;
    private double initialBalance;
    private double accruedInterest;
    private double interestEarned;
    private double annualEffectiveRate;
    private double annualEffectiveRateReturn;
    private Integer numberRepresentatives;
    private Integer numberSigners;
    private String currency;
    private boolean status = true;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date cancelAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastMovement;

    public Debit(Client client, List<Client> representatives, List<Client> signers, String account, Product product, double balance, double initialBalance, double accruedInterest, double interestEarned, double annualEffectiveRate, double annualEffectiveRateReturn, Integer numberRepresentatives, Integer numberSigners, String currency, boolean status, Date createAt, Date cancelAt, Date lastMovement) {
        this.client = client;
        this.representatives = representatives;
        this.signers = signers;
        this.account = account;
        this.product = product;
        this.balance = balance;
        this.initialBalance = initialBalance;
        this.accruedInterest = accruedInterest;
        this.interestEarned = interestEarned;
        this.annualEffectiveRate = annualEffectiveRate;
        this.annualEffectiveRateReturn = annualEffectiveRateReturn;
        this.numberRepresentatives = numberRepresentatives;
        this.numberSigners = numberSigners;
        this.currency = currency;
        this.status = status;
        this.createAt = createAt;
        this.cancelAt = cancelAt;
        this.lastMovement = lastMovement;
    }
}
