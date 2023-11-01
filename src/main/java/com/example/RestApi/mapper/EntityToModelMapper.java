package com.example.RestApi.mapper;

import com.example.RestApi.domain.Customer;
import com.example.RestApi.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToModelMapper {
    Customer CustomerEntityToCustomer(CustomerEntity customerEntity);
    CustomerEntity CustomerToCustomerEntity(Customer customer);
}
