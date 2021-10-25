package com.finance.debitms.dao.persistence;

import com.finance.debitms.domain.document.Debit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface DebitPersistenceRepository extends ReactiveMongoRepository<Debit, String> {
    Mono<Debit> findByClientId(String clientId);
}
