package com.webwork.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.webwork.currencyconversionservice.beans.CurrencyConversionBeans;
import com.webwork.currencyconversionservice.beans.CurrencyExchangeBeans;
import com.webwork.currencyconversionservice.beans.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {
	
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
    
    private static final String CURRENCY_EXCHANGE_URL = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";
    
    
    
    private CurrencyExchangeProxy proxy;


    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBeans currencyConverter(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        
        // Log statements
        logger.info("This is an INFO log message");
        logger.debug("This is a DEBUG log message");
        logger.warn("This is a WARN log message");
        logger.error("This is an ERROR log message");
        
        // Call the currency exchange service
        ResponseEntity<CurrencyExchangeBeans> responseEntity = new RestTemplate().getForEntity(
        		CURRENCY_EXCHANGE_URL, 
            CurrencyExchangeBeans.class, 
            uriVariables
        );
        
        CurrencyExchangeBeans response = responseEntity.getBody();
        
        // Check for null response
        if (response == null) {
            throw new RuntimeException("Unable to get currency exchange information");
        }
        
        // Create and return the CurrencyConversionBeans object
        return new CurrencyConversionBeans(
            response.getId(),
            from,
            to,
            response.getConversionMultiple(),
            quantity,
            quantity.multiply(response.getConversionMultiple()),
            response.getPort()
        );
    }
    
    @GetMapping("/currency-converter/feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBeans currencyfeignConverter(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        
        // Log statements
        logger.info("This is an INFO log message");
        logger.debug("This is a DEBUG log message");
        logger.warn("This is a WARN log message");
        logger.error("This is an ERROR log message");
         
        CurrencyConversionBeans response = (CurrencyConversionBeans) proxy.retriveExchangeValue(from, to);
        
        // Check for null response
        if (response == null) {
            throw new RuntimeException("Unable to get currency exchange information");
        }
        
        // Create and return the CurrencyConversionBeans object
        return new CurrencyConversionBeans(
            response.getId(),
            from,
            to,
            response.getConversionMultiple(),
            quantity,
            quantity.multiply(response.getConversionMultiple()),
            response.getPort()
        );
    }
}
