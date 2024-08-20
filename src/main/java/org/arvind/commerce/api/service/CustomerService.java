package org.arvind.commerce.api.service;

import lombok.extern.slf4j.Slf4j;
import org.arvind.commerce.api.exception.ResourceNotFoundException;
import org.arvind.commerce.api.model.Customer;
import org.arvind.commerce.api.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Customer getCustomerById(Long id) {
        Optional<Customer> optional = customerRepository.findById(id);

        if(optional.isEmpty()) {
            log.error("Customer with id {} not found", id);
            throw new ResourceNotFoundException("String.format(\"Customer with identifier %1$s not found\", id)");
        }

        return optional.get();
    }

    public Long createCustomer(Customer customer) {
        return customerRepository.save(customer).getId();
    }

}
