package com.nikitha.microservices.currency_exchange_service.controller;

import com.nikitha.microservices.currency_exchange_service.Repo.CurrencyExchangeRepository;
import com.nikitha.microservices.currency_exchange_service.model.CurrencyExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to)
    {
            CurrencyExchange currencyExchange=
                    repository.findByFromAndTo(from,to);
            if(currencyExchange==null)
            {
                throw new RuntimeException("Unable to find data for "+ from + " to " + to);
            }
            String port =environment.getProperty("local.server.port");
            currencyExchange.setEnvironment(port);


            return currencyExchange;
    }

}
