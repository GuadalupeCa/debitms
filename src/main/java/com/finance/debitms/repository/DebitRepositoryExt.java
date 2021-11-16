package com.finance.debitms.repository;

import com.finance.debitms.domain.document.Debit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitRepositoryExt extends ReactiveMongoRepository<Debit, String> {
    Flux<Debit> findByClientDocumentIdentityNumberAndStatusTrue(String documentIdentityNumber);
    Flux<Debit> findByAccount(String account);
}
