package com.finance.debitms.repository;

import com.finance.debitms.document.Debit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DebitRepositoryImplements implements DebitRepositoryInterface {

    @Autowired
    private DebitRepository debitRepository;

    @Override
    public Flux findAll() {
        return debitRepository.findAll();
    }

    @Override
    public Mono findById(String id) {
        return debitRepository.findById(id);
    }

    @Override
    public Mono findByClientId(String clientId) {
        return debitRepository.findByClientId(clientId);
    }

    @Override
    public Mono save(Debit debit) {
        return debitRepository.save(debit);
    }
}
