package com.finance.debitms.repository;

import com.finance.debitms.document.Debit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitRepository extends ReactiveMongoRepository<Debit, String> {
    Mono<Debit> findByClientId(String clientId);
}
