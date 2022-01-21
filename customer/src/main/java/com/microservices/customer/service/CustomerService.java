package com.microservices.customer.service;

import com.microservices.clients.fraud.FraudCheckResponse;
import com.microservices.clients.fraud.FraudClient;
import com.microservices.customer.dto.CustomerRequest;
import com.microservices.customer.model.Customer;
import com.microservices.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public record CustomerService(CustomerRepository repository, FraudClient fraudClient) {

    public void registerCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        //TODO: check if email is valid
        //TODO: check if email not taken

        repository.saveAndFlush(customer);
        FraudCheckResponse response = fraudClient.isFraudster(customer.getId());

        if (response.isFraudster()){
            throw new IllegalStateException("Fraudster");
        }
    }
}
