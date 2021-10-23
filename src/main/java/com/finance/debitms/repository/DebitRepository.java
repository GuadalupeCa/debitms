package com.finance.debitms.repository;

import com.finance.debitms.document.Debit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DebitRepository extends ReactiveMongoRepository<Debit, String> {
    Flux findByClientId(String clientId);

}
