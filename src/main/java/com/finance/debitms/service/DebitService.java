package com.finance.debitms.service;

import com.finance.debitms.dao.persistence.DebitPersistenceRepository;
import com.finance.debitms.domain.document.Debit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DebitService{

    @Autowired
    private DebitPersistenceRepository debitRepositoryInterface;

    public Flux findAll(){
        return debitRepositoryInterface.findAll();
    }

    public Mono findById(String id){
        return debitRepositoryInterface.findById(id);
    }

    public Mono<Debit> findByClientId(String clientId){
        return debitRepositoryInterface.findByClientId(clientId);
    }

    public Mono save(Debit debit){
        return debitRepositoryInterface.save(debit);
    }

}
