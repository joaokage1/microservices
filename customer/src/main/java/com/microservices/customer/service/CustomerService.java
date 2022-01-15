package com.microservices.customer.service;

import com.microservices.customer.dto.CustomerRequest;
import com.microservices.customer.model.Customer;
import com.microservices.customer.model.FraudCheckResponse;
import com.microservices.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public record CustomerService(CustomerRepository repository, RestTemplate restTemplate) {

    public void registerCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        //TODO: check if email is valid
        //TODO: check if email not taken

        repository.saveAndFlush(customer);

        FraudCheckResponse response = restTemplate.getForObject("http://FRAUD/api/v1/fraud-check/{customerId}", FraudCheckResponse.class, customer.getId());

        if (response.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
    }
}
