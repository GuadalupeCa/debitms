package com.finance.debitms;

import com.finance.debitms.handler.DebitHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class FunctionalRoute {

    @Bean
    public RouterFunction route(DebitHandler debitHandler) {
        return RouterFunctions
                .route(GET("/debit").and(accept(MediaType.APPLICATION_JSON)), debitHandler::findAll)
                .andRoute(GET("/debit/{id}").and(accept(MediaType.APPLICATION_JSON)), debitHandler::findById)
                .andRoute(POST("/debit/save").and(accept(MediaType.APPLICATION_JSON)), debitHandler::save)
                .andRoute(PUT("/debit/update").and(accept(MediaType.APPLICATION_JSON)), debitHandler::update)
                .andRoute(DELETE("/debit/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), debitHandler::deleteById);
    }
}
