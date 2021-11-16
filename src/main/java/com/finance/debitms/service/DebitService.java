package com.finance.debitms.service;

import com.finance.debitms.domain.document.Client;
import com.finance.debitms.domain.document.Debit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitService {
    Flux<Debit> findAll();
    Mono<Debit> findById(String id);
    Mono<Debit> save(Debit debit);
    Mono<Void> deleteById(String id);
    Flux<Debit> findByClientDocumentIdentityNumber(String documentIdentityNumber);
    Flux<Debit> findByAccount(String account);
}
