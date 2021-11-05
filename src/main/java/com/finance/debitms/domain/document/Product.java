package com.finance.debitms.domain.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Product{

    private String name;
    private String type;

    private Boolean commissionMaintenance;
    private Integer amountCommissionMaintenance;

    private Boolean maximumLimitMonthlyMovements;
    private Integer quantityMaximumLimitMonthlyMovements;

    private Boolean allowDeposit;
    private Boolean allowWithdrawal;

    private Integer maximumQuantityCredit;
    private Integer maximumQuantityDebit;
}
