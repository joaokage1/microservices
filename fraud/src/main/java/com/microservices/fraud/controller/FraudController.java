package com.microservices.fraud.controller;

import com.microservices.clients.fraud.FraudCheckResponse;
import com.microservices.fraud.service.FraudCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fraud-check")
@Slf4j
public record FraudController(FraudCheckService service) {

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable Integer customerId){
        log.info("Fraud check request for customer {}", customerId);
        return new FraudCheckResponse(service.isFraudulentCustomer(customerId));
    }
}
