package com.finance.debitms.handler;

import com.finance.debitms.domain.document.Debit;
import com.finance.debitms.domain.document.Product;
import com.finance.debitms.service.DebitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DebitHandler {

    @Autowired
    private DebitService debitService;

    public Mono findAll(ServerRequest serverRequest) {
        log.info("Find all clients");
        return ServerResponse.ok()
                .body(debitService.findAll(), Debit.class);
    }

    public Mono findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.info("Find by Id: {}", id);
        return ServerResponse.ok().body(debitService.findById(id), Debit.class);
    }

    public Mono findByClientId(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.info("Find products by ClientId: {}", id);
        Mono<Debit> debit = debitService.findByClientId(id);

        return debit.flatMap(d -> WebClient.create()
                .get()
                .uri("http://localhost:8081/product/{id})", d.getProductId())
                .retrieve()
                .bodyToMono(Product.class));
    }

    public Mono save(ServerRequest serverRequest) {
        Mono<Debit> debit = serverRequest.bodyToMono(Debit.class);
        log.info("Save debit");
        return debit.flatMap( d -> ServerResponse
                .status(HttpStatus.CREATED)
                .body( debitService.save(d), Debit.class));
    }

    public Mono update(ServerRequest serverRequest) {
        Mono<Debit> debit = serverRequest.bodyToMono(Debit.class);
        log.info("Update debit");
        return debit.flatMap( d -> ServerResponse
                .status(HttpStatus.CREATED)
                .body( debitService.save(d), Debit.class));
    }

    public Mono deleteById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.info("Delete debit by id");
        return debitService.deleteById(id);
    }
}
