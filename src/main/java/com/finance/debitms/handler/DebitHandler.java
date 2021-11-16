package com.finance.debitms.handler;

import com.finance.debitms.domain.document.Client;
import com.finance.debitms.domain.document.Debit;
import com.finance.debitms.domain.document.Product;
import com.finance.debitms.service.DebitService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Component
public class DebitHandler {

    @Autowired
    private DebitService debitService;

    CircuitBreaker circuitBreaker = CircuitBreaker.of("DEBITSERVICE", CircuitBreakerConfig.ofDefaults());

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

    public Mono save(ServerRequest serverRequest) {
        Map<String, Object> responseMessage = new HashMap<>();
        Mono<Debit> debit = serverRequest.bodyToMono(Debit.class);
        responseMessage.put("Note", "No se puede proceder con el proceso de registro");

        return debit
                .flatMap(debitToSave -> {
                    log.info("Validate document identity number");

                    if(debitToSave.getClient() == null ) {
                        log.info("no cliente");

                        responseMessage.put("Message", "Los datos del cliente son necesarios");
                        return ServerResponse.status(HttpStatus.NO_CONTENT)
                                .body(BodyInserters.fromValue(responseMessage));

                    } else if (debitToSave.getClient().getDocumentIdentityNumber() != null) {
                        log.info("check existence of the client");
                        Flux<Client> clientData = WebClient.builder().build().get()
                                .uri("http://localhost:8080/client/ide/{ide}", debitToSave.getClient().getDocumentIdentityNumber())
                                .retrieve()
                                .bodyToFlux(Client.class);

                        clientData.collectList().flatMap(c -> {
                           if(c.isEmpty()){
                               log.info("regitrar nuevo cliente");
                                return WebClient.builder().build().post()
                                       .uri("http://localhost:8080/client/save")
                                       .contentType(MediaType.APPLICATION_JSON)
                                       .bodyValue(debitToSave.getClient())
                                        .retrieve()
                                        .bodyToMono(Client.class);
                           } else return Mono.empty();
                        }).subscribe();
                    }

                    if(debitToSave.getRepresentatives() != null){

                        Long numberRepresentatives = debitToSave.getRepresentatives().stream().count();

                        if(numberRepresentatives > 0) {
                            log.info("Validate data of representatives");

                            debitToSave.getRepresentatives().stream()
                                    .forEach(representative -> {
                                        log.info("v" +representative.getDocumentIdentityNumber());
                                        if(representative.getType().getName().toUpperCase() != "EMPRESARIAL" & representative.getDocumentIdentityNumber() != null) {
                                            Flux<Client> representativeData = WebClient.builder().build().get()
                                                    .uri("http://localhost:8080/client/ide/{ide}", representative.getDocumentIdentityNumber())
                                                    .retrieve()
                                                    .bodyToFlux(Client.class);

                                            representativeData.collectList().flatMap(r -> {
                                                if (r.isEmpty()) {
                                                    log.info("regitrar nuevo representante");
                                                    return WebClient.builder().build().post()
                                                            .uri("http://localhost:8080/client/save")
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .bodyValue(representative)
                                                            .retrieve()
                                                            .bodyToMono(Client.class );
                                                } else return Mono.just(r);
                                            }).subscribe();
                                        }
                                        log.info("validar tipo de representante");

                                    });
                        }
                    }

                    if(debitToSave.getSigners() != null) {
                        Long numberSigners = debitToSave.getSigners().stream().count();

                        if (numberSigners > 0) {
                            log.info("Validate data of the signers if it has");

                            debitToSave.getSigners().stream()
                                    .forEach(signer -> {
                                        if (signer.getDocumentIdentityNumber().isEmpty()) {
                                            responseMessage.put("Message", "Es necesario tener el numero de documento de identidad del (de los) firmante(s).");
//                                            return ServerResponse.status(HttpStatus.NO_CONTENT)
//                                                    .body(BodyInserters.fromValue(responseMessage));
                                        } else if (signer.getType().getName().toUpperCase() != "EMPRESARIAL" & signer.getDocumentIdentityNumber() != null) {
                                            Flux<Client> representativeData = WebClient.builder().build().get()
                                                    .uri("http://localhost:8080/client/ide/{ide}", signer.getDocumentIdentityNumber())
                                                    .retrieve()
                                                    .bodyToFlux(Client.class);

                                            representativeData.collectList().flatMap(r -> {
                                                if (r.isEmpty()) {
                                                    log.info("regitrar nuevo representante");
                                                    return WebClient.builder().build().post()
                                                            .uri("http://localhost:8080/client/save")
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .bodyValue(signer)
                                                            .retrieve()
                                                            .bodyToMono(Client.class);
                                                } else return Mono.just(r);
                                            }).subscribe();
                                        }
                                    });
                        }
                    }

                    log.info("Validate existed debits account of the client.");
                    Flux<Debit> clientDebits = debitService.findByClientDocumentIdentityNumber(debitToSave.getClient().getDocumentIdentityNumber());

                    return clientDebits.collectList().flatMap(accounts -> {
                        log.info(String.valueOf(accounts));
                        boolean save = false;
                        switch(debitToSave.getClient().getType().getName().toUpperCase()){
                            case "PERSONAL": {
                                if (accounts.isEmpty())  save = true;
                                else {
                                    responseMessage.put("Message", "El cliente no puede aperturar mas de una cuenta de debito");
                                }
                            }
                            case "EMPRESARIAL": {
                                if (debitToSave.getProduct().getName().equalsIgnoreCase("CUENTA CORRIENTE") & debitToSave.getRepresentatives().stream().count() > 0) save = true;
                                else {
                                    responseMessage.put("Message", "El cliente empresarial solo puede aperturar cuentas corrientes y tener al menos un representante");
                                }
                            }
                        }
                        if (save) {
                            log.info("Save debit account.");
                            return ServerResponse.ok().body(debitService.save(debitToSave), Debit.class);
                        } else {
                            return ServerResponse.status(HttpStatus.NO_CONTENT)
                                    .body(BodyInserters.fromValue(responseMessage));
                        }
                    });
                });
    }

//    @CircuitBreaker(name = DEBIT_SERVICE, fallbackMethod = "debitUpdateFallback")
    public Mono update(ServerRequest serverRequest) {
        Map<String, Object> response = new HashMap<>();
        Mono<Debit> debit = serverRequest.bodyToMono(Debit.class);
        response.put("Note", "No se puede proceder con el proceso de actualizacion.");

        return debit
                .flatMap(debitToUpdate -> {
                    //validate the number of the representatives
                    if(debitToUpdate.getClient().getType().getName().equalsIgnoreCase("EMPRESARIAL")) {

                        List<Client> representativesData = new ArrayList<>();
                        Long numberRepresentatives = debitToUpdate.getRepresentatives().stream().count();

                        if(numberRepresentatives > 0) {
                            debitToUpdate.getRepresentatives().stream()
                                    .forEach(representative -> {
                                        if(representative.getType().getName().toUpperCase() != "EMPRESARIAL" & representative.getDocumentIdentityNumber() != null) {

                                            Flux<Client> representativeData = WebClient.builder().build().get()
                                                    .uri("http://localhost:8080/client/ide/{ide}", representative.getDocumentIdentityNumber())
                                                    .retrieve()
                                                    .bodyToFlux(Client.class)
                                                    .transform(CircuitBreakerOperator.of(circuitBreaker))
                                                    .onErrorResume(error -> clientFallback());

                                            log.info("validar tipo de representante");

                                            representativeData.map(c -> {
                                                        if (c.getName().equalsIgnoreCase("NO_NAME")) {
                                                            log.info("entro");
                                                            return ServerResponse.status(HttpStatus.NO_CONTENT)
                                                                    .body(BodyInserters.fromValue(response));

                                                        }
                                                        else return c;
                                                    })
                                                    .collectList()
                                                    .flatMap(r -> {
                                                        if (r.isEmpty()) {
                                                            log.info("regitrar nuevo representante");
                                                            return WebClient.builder().build().post()
                                                                    .uri("http://localhost:8080/client/save")
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .bodyValue(representative)
                                                                    .retrieve()
                                                                    .bodyToMono(Client.class)
                                                                    .transform(CircuitBreakerOperator.of(circuitBreaker))
                                                                    .onErrorResume(error -> clientFallback());
                                                            } else return Mono.just(r);
                                                        })
                                                    .subscribe();
                                        }
                                    });
                        } else {
                            response.put("Message", "Se debe tener al menos un representante de la cuenta empresarial.");
                            return ServerResponse.status(HttpStatus.NO_CONTENT)
                                    .body(BodyInserters.fromValue(response));
                        }
                    }
                    return ServerResponse
                            .status(HttpStatus.CREATED)
                            .body( debitService.save(debitToUpdate), Debit.class);
                });
    }

    public Mono deleteById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.info("Delete debit by id");
        return debitService.deleteById(id);
    }

    public Mono<ServerResponse> findByClientDocumentIdentityNumber(ServerRequest serverRequest){
        String documentIdentityNumber = serverRequest.pathVariable("ide");

        return ServerResponse
                .status(HttpStatus.CREATED)
                .body(debitService.findByClientDocumentIdentityNumber(documentIdentityNumber),Debit.class);
    }

    public Mono findByAccount(ServerRequest serverRequest) {
        String account = serverRequest.pathVariable("account");
        log.info("Find by Account: {}", account);
        return ServerResponse.ok().body(debitService.findByAccount(account), Debit.class);
    }

    public Mono<Debit> debitFallback() {
        log.info("circuit");
        return Mono.just(Debit
                .builder()
                .id("FAILED")
                .client(Client.builder()
                        .name("NO_NAME")
                        .documentIdentityNumber("NO_NUMBER").build())
                .account("UNKNOWN_ACCOUNT")
                .product(Product.builder()
                        .name("NO_NAME").build())
                .balance(0)
                .build());
    }

    public Mono<Client> clientFallback() {
        log.info("circuit client");
        return Mono.just(Client
                .builder()
                .id("FAILED")
                .name("NO_NAME")
                .documentIdentityNumber("NO_NUMBER")
                .build());
    }
}
