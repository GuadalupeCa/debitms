package com.finance.debitms.repository.crud;

import com.finance.debitms.document.Debit;
import com.finance.debitms.repository.DebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DebitRepositoryCrud implements DebitCrud{

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
//
//    @Override
//    public Flux findByClientId(String clientId) {
//        return debitRepository.findByClientId(clientId);
//    }

    @Override
    public Mono save(Debit debit) {
        return debitRepository.save(debit);
    }
}
