package com.finance.debitms.handler;

import com.finance.debitms.domain.document.Client;
import com.finance.debitms.domain.document.Debit;
import com.finance.debitms.domain.document.Product;
import com.finance.debitms.service.DebitService;
import com.mongodb.internal.connection.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.rmi.ServerError;
import java.util.*;

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

    public Mono save(ServerRequest serverRequest) {
        Map<String, Object> response = new HashMap<>();
        Mono<Debit> debit = serverRequest.bodyToMono(Debit.class);
        response.put("Note", "No se puede proceder con el proceso de registro");

        return debit
            .flatMap(debitToSave -> {
                Client clientData;
                log.info("Validate document identity number");

                if (debitToSave.getClient().getDocumentIdentityNumber().isEmpty()) {
                    log.info("entra");

                    response.put("Message", "Es necesario tener el numero de documento de identidad del cliente");
                    return ServerResponse.status(HttpStatus.NO_CONTENT)
                            .body(BodyInserters.fromValue(response));
                } else if(debitToSave.getClient().getType().getName().isEmpty()){
                    log.info("entr2");

                    clientData = WebClient.builder().build().get()
                            .uri("http://localhost:8080/client/ide/{ide}", debitToSave.getClient().getDocumentIdentityNumber())
                            .retrieve()
                            .bodyToMono(Client.class).block();
                } else clientData = debitToSave.getClient();

                debitToSave.setClient(clientData);

                log.info("Validate data of the representatives if it has");
                if(debitToSave.getRepresentatives() != null){
                    Long numberRepresentatives = debitToSave.getRepresentatives().stream().count();

                    if(numberRepresentatives > 0) {
                        List<Client> representativesData = new ArrayList<>();

                        debitToSave.getRepresentatives().stream()
                                .map(c -> {
                                    if(c.getDocumentIdentityNumber().isEmpty()){
                                        response.put("Message", "Es necesario tener el numero de documento de identidad del (de los) representante(s).");
                                        return ServerResponse.status(HttpStatus.NO_CONTENT)
                                                .body(BodyInserters.fromValue(response));
                                    } else if(c.getType().getName().isEmpty()){
                                        Client representativeData = WebClient.builder().build().get()
                                                .uri("http://localhost:8080/client/ide/{ide}", c.getDocumentIdentityNumber())
                                                .retrieve()
                                                .bodyToMono(Client.class).block();
                                        if(representativeData.getType().getName().toUpperCase() == "EMPRESARIAL") {
                                            response.put("Message", "El(los) representante(s) de la cuenta empresarial debe ser una persona natural.");
                                            return ServerResponse.status(HttpStatus.NO_CONTENT)
                                                    .body(BodyInserters.fromValue(response));
                                        } else representativesData.add(representativeData);
                                    } else representativesData.add(c);

                                    return representativesData;
                                });

                        debitToSave.setRepresentatives(representativesData);
                    }
                }

                log.info("Validate data of the signers if it has");
                if(debitToSave.getSigners() != null) {
                    Long numberSigners = debitToSave.getSigners().stream().count();

                    if (numberSigners > 0) {
                        List<Client> signersData = new ArrayList<>();

                        debitToSave.getRepresentatives().stream().map(c -> {
                            if (c.getDocumentIdentityNumber().isEmpty()) {
                                response.put("Message", "Es necesario tener el numero de documento de identidad del (de los) firmante(s).");
                                return ServerResponse.status(HttpStatus.NO_CONTENT)
                                        .body(BodyInserters.fromValue(response));
                            } else if (c.getType().getName().isEmpty()) {
                                Client signerData = WebClient.builder().build().get()
                                        .uri("http://localhost:8080/client/ide/{ide}", c.getDocumentIdentityNumber())
                                        .retrieve()
                                        .bodyToMono(Client.class).block();

                                if (signerData.getType().getName().toUpperCase() == "EMPRESARIAL") {
                                    response.put("Message", "El(los) firmante(s) de la cuenta empresarial debe ser una persona natural.");
                                    return ServerResponse.status(HttpStatus.NO_CONTENT)
                                            .body(BodyInserters.fromValue(response));
                                } else signersData.add(signerData);
                            } else signersData.add(c);

                            return signersData;
                        });

                        debitToSave.setSigners(signersData);
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
                                response.put("Message", "El cliente no puede aperturar mas de una cuenta de debito");
                            }
                        }
                        case "EMPRESARIAL": {
                            if (debitToSave.getProduct().getName().toUpperCase() == "CUENTA CORRIENTE" & debitToSave.getRepresentatives().stream().count() > 0) save = true;
                            else {
                                response.put("Message", "El cliente solo puede aperturar cuentas corrientes");
                            }
                        }
                    }
                    if (save) {
                        log.info("Save debit account.");
                        return ServerResponse.ok().body(debitService.save(debitToSave), Debit.class);
                    } else {
                        return ServerResponse.status(HttpStatus.NO_CONTENT)
                                .body(BodyInserters.fromValue(response));
                    }
                });
            });
    }

    public Mono update(ServerRequest serverRequest) {
        Map<String, Object> response = new HashMap<>();
        Mono<Debit> debit = serverRequest.bodyToMono(Debit.class);
        response.put("Note", "No se puede proceder con el proceso de actualizacion.");

        return debit
                .flatMap(debitToUpdate -> {
                    //validate the number of the representatives
                    if(debitToUpdate.getClient().getType().getName().toUpperCase() == "EMPRESARIAL") {

                        List<Client> representativesData = new ArrayList<>();
                        Long numberRepresentatives = debitToUpdate.getRepresentatives().stream().count();

                        if(numberRepresentatives > 0) {
                            debitToUpdate.getRepresentatives().stream()
                                    .map(c -> {
                                        if(c.getDocumentIdentityNumber().isEmpty()){
                                            response.put("Message", "Es necesario tener el numero de documento de identidad del (de los) representante(s).");
                                            return ServerResponse.status(HttpStatus.NO_CONTENT)
                                                    .body(BodyInserters.fromValue(response));
                                        } else if(c.getType().getName().isEmpty()){
                                            Client representativeData = WebClient.builder().build().get()
                                                    .uri("http://localhost:8080/client/ide/{ide}", c.getDocumentIdentityNumber())
                                                    .retrieve()
                                                    .bodyToMono(Client.class).block();
                                            if(representativeData.getType().getName().toUpperCase() == "EMPRESARIAL") {
                                                response.put("Message", "El(los) representante(s) de la cuenta empresarial debe ser una persona natural.");
                                                return ServerResponse.status(HttpStatus.NO_CONTENT)
                                                        .body(BodyInserters.fromValue(response));
                                            } else representativesData.add(representativeData);
                                        } else representativesData.add(c);

                                        return representativesData;
                                    });

                            debitToUpdate.setRepresentatives(representativesData);
                            log.info("Update debit");
                            return debit.flatMap( d -> ServerResponse
                                    .status(HttpStatus.CREATED)
                                    .body( debitService.save(d), Debit.class));
                        } else {
                            response.put("Message", "Se debe tener al menos un representante de la cuenta empresarial.");
                            return ServerResponse.status(HttpStatus.NO_CONTENT)
                                    .body(BodyInserters.fromValue(response));
                        }
                    }
                    return debit.flatMap( d -> ServerResponse
                            .status(HttpStatus.CREATED)
                            .body( debitService.save(d), Debit.class));
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

}
