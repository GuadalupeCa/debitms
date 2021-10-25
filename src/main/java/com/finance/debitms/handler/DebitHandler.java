package com.finance.debitms.handler;

import com.finance.debitms.domain.document.Debit;
import com.finance.debitms.domain.document.Product;
import com.finance.debitms.service.DebitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/debit")
@RefreshScope
public class DebitHandler {

    @Autowired
    private DebitService debitService;

    @GetMapping("/")
    public Flux findAll() {
        log.info("Find all clients");
        return debitService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Debit> findById(@PathVariable("id") String id) {
        log.info("Find by Id: {}", id);
        return debitService.findById(id);
    }

    @GetMapping("/client/{id}")
    public Mono findByClientId(@PathVariable("id") String clientId) {
        log.info("Find products by ClientId: {}", clientId);
        Mono<Debit> debit = debitService.findByClientId(clientId);

        return debit.flatMap(d -> WebClient.create()
                .get()
                .uri("http://localhost:8081/product/{id})", d.getProductId())
                .retrieve()
                .bodyToMono(Mono.class));
    }

    @PostMapping("/save")
    public void createEmp(@RequestBody Debit debit) {
        debitService.save(debit).subscribe();
    }
}
