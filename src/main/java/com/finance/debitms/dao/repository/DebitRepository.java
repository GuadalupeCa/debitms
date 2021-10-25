package com.finance.debitms.dao.repository;

import com.finance.debitms.dao.persistence.DebitPersistenceRepository;
import com.finance.debitms.domain.document.Debit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DebitRepository implements DebitRepositoryInterface {

    @Autowired
    private DebitPersistenceRepository debitRepository;

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
