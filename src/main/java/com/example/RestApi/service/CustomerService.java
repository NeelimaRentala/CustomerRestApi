package com.example.RestApi.service;

import com.example.RestApi.RestApiApplication;
import com.example.RestApi.domain.Customer;
import com.example.RestApi.exception.CustomerNotFoundException;
import com.example.RestApi.mapper.EntityToModelMapper;
import com.example.RestApi.repository.CustomerRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(RestApiApplication.class);

    private CustomerRepository customerRepository;
    private EntityToModelMapper mapper;

    public CustomerService(CustomerRepository customerRepository, EntityToModelMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public Customer findCustomers(String customerRef) {
        return customerRepository.findById(customerRef).map(customerEntity -> mapper.CustomerEntityToCustomer(customerEntity))
                .orElseThrow(() -> new CustomerNotFoundException(customerRef));
    }

    public void save(Customer customer) {
        customerRepository.save(mapper.CustomerToCustomerEntity(customer));
    }
}
