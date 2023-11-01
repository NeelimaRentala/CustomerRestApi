package com.example.RestApi.controller;

import com.example.RestApi.domain.Customer;
import com.example.RestApi.exception.CustomerNotFoundException;
import com.example.RestApi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest
class CustomerControllerTest {

    private static final String VALID_REFERENCE_ID = "1";
    private static final String INVALID_REFERENCE_ID = "-100012";

    @MockBean
    private CustomerService customerServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPing_shouldReturnResponse() throws Exception {

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Hello!", result.getResponse().getContentAsString());
    }

    @Test
    void testFindCustomer_whenValidReferenceIsPassed_shouldSucceed() throws Exception {

        Customer customer = new Customer(VALID_REFERENCE_ID, "John", "Tick street", "Walker Road", "Rainham", "essex", "UK", "W1T3NL");
        when(customerServiceMock.findCustomers(VALID_REFERENCE_ID)).thenReturn(customer);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + VALID_REFERENCE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerReference").value(VALID_REFERENCE_ID))
                .andExpect(jsonPath("$.customerName").value("John"))
                .andExpect(jsonPath("$.addressLine1").value("Tick street"))
                .andExpect(jsonPath("$.addressLine2").value("Walker Road"))
                .andExpect(jsonPath("$.town").value("Rainham"))
                .andExpect(jsonPath("$.county").value("essex"))
                .andExpect(jsonPath("$.country").value("UK"))
                .andExpect(jsonPath("$.postCode").value("W1T3NL"))
                .andReturn();
    }

    @Test
    void testFindCustomer_whenNotExistingReferenceIsPassed_shouldThrowException() throws Exception {

        when(customerServiceMock.findCustomers(INVALID_REFERENCE_ID))
                .thenThrow(CustomerNotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + INVALID_REFERENCE_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

}