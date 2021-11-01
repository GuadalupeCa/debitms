package com.finance.debitms.repository;

import com.finance.debitms.domain.document.Debit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitRepository {
    Flux<Debit> findAll();
    Mono<Debit> findById(String id);
    Mono<Debit> findByClientId(String clientId);
    Mono<Debit> save(Debit debit);
    Mono<Void> deleteById(String id);
}
