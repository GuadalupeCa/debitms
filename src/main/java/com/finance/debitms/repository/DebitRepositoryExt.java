package com.finance.debitms.repository;

import com.finance.debitms.domain.document.Debit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface DebitRepositoryExt extends ReactiveMongoRepository<Debit, String> {
}