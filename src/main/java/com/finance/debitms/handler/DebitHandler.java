package com.finance.debitms.handler;

import com.finance.debitms.document.Debit;
import com.finance.debitms.repository.DebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/debit")
public class DebitHandler {

    @Autowired
    private DebitRepository debitRepository;


    @GetMapping("/")
    public Flux findAll() {
        log.info("Find all clients");
        return debitRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Debit> findById(@PathVariable("id") String id) {
        log.info("Find by Id: {}", id);
        return debitRepository.findById(id);
    }

    @GetMapping("/client/{id}")
    public Mono findByClientId(@PathVariable("id") String clientId) {
        log.info("Find products by ClientId: {}", clientId);

        var restTemplate = new RestTemplate();
        Flux debits = debitRepository.findByClientId(clientId);

        debits.subscribe( debit -> restTemplate.getForObject("http://localhost:8081/product/{" + debit() + "}", Mono.class));


        return debit;
    }

    @PostMapping("/save")
    public void createEmp(@RequestBody Debit debit) {
        debitRepository.save(debit).subscribe();
    }
}
