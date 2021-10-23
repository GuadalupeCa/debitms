package com.finance.debitms.repository.crud;

import com.finance.debitms.document.Debit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCrud {
    Flux findAll();
    Mono findById(String id);
    //Flux findByClientId(String clientId);
    Mono save(Debit debit);
}
