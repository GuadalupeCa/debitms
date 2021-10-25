package com.finance.debitms.dao.repository;

import com.finance.debitms.domain.document.Debit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitRepositoryInterface {
    Flux findAll();
    Mono findById(String id);
    Mono findByClientId(String clientId);
    Mono save(Debit debit);
}
