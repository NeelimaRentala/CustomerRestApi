package com.example.RestApi.service;

import com.example.RestApi.domain.Customer;
import com.example.RestApi.entity.CustomerEntity;
import com.example.RestApi.exception.CustomerNotFoundException;
import com.example.RestApi.mapper.EntityToModelMapper;
import com.example.RestApi.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceUnitTest {

    private static final String VALID_REFERENCE_ID = "1";
    private static final String INVALID_REFERENCE_ID = "-100012";

    @Mock
    private CustomerRepository customerRepositoryMock;
    @Mock
    private EntityToModelMapper mapperMock;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testSave_whenCustomerIsPassed_shouldSaveInDbAndNotThrowException() {
        when(mapperMock.CustomerToCustomerEntity(any(Customer.class)))
                .thenReturn(new CustomerEntity());
        customerService.save(new Customer());
        Assertions.assertDoesNotThrow(() -> customerService.save(new Customer()));
    }

    @Test
    void testFindCustomers_whenInvalidCustomerRefIsPassed_shouldThrowException() {

        when(customerRepositoryMock.findById(INVALID_REFERENCE_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomers(INVALID_REFERENCE_ID));
    }

}