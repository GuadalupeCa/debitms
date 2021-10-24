package com.finance.debitms.service;

import com.finance.debitms.document.Debit;
import com.finance.debitms.repository.DebitRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DebitService{

    @Autowired
    private DebitRepositoryInterface debitCrud;

    public Flux findAll(){
        return debitCrud.findAll();
    }

    public Mono findById(String id){
        return debitCrud.findById(id);
    }

    public Mono findByClientId(String clientId){
        return debitCrud.findByClientId(clientId);
    }

    public Mono save(Debit debit){
        return debitCrud.save(debit);
    }

}
