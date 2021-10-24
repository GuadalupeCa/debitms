package com.finance.debitms.repository;

import com.finance.debitms.document.Debit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitRepositoryInterface {
    Flux findAll();
    Mono findById(String id);
    Mono findByClientId(String clientId);
    Mono save(Debit debit);
}
