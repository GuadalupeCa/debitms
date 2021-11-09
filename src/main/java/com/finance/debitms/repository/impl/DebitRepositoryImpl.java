package com.finance.debitms.repository.impl;

import com.finance.debitms.domain.document.Debit;
import com.finance.debitms.repository.DebitRepositoryExt;
import com.finance.debitms.repository.DebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DebitRepositoryImpl implements DebitRepository {

    @Autowired
    private DebitRepositoryExt debitRepository;

    @Override
    public Flux<Debit> findAll() {
        return debitRepository.findAll();
    }

    @Override
    public Mono<Debit> findById(String id) {
        return debitRepository.findById(id);
    }

    @Override
    public Mono<Debit> save(Debit debit) {
        return debitRepository.save(debit);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return debitRepository.deleteById(id);
    }

    @Override
    public Flux<Debit> findByClientDocumentIdentityNumber(String documentIdentityNumber) {
        return debitRepository.findByClientDocumentIdentityNumber(documentIdentityNumber);
    }
}
