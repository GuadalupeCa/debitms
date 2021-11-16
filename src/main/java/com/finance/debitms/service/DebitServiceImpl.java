package com.finance.debitms.service;

import com.finance.debitms.repository.DebitRepository;
import com.finance.debitms.domain.document.Debit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class DebitServiceImpl implements DebitService{
    @Autowired
    private DebitRepository debitRepository;

    public Flux<Debit> findAll(){
        return debitRepository.findAll();
    }

    public Mono<Debit> findById(String id){
        return debitRepository.findById(id);
    }

    public Mono<Debit> save(Debit debit){
        return debitRepository.save(debit);
    }

    public Mono<Void> deleteById(String id){
        return debitRepository.deleteById(id);
    }

    public Flux<Debit> findByClientDocumentIdentityNumber(String documentIdentityNumber){
        return debitRepository.findByClientDocumentIdentityNumber(documentIdentityNumber);
    }

    public Flux<Debit> findByAccount(String account){
        return debitRepository.findByAccount(account);
    }
}
