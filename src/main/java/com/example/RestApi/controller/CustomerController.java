package com.example.RestApi.controller;

import com.example.RestApi.csv.CSVUtility;
import com.example.RestApi.domain.Customer;
import com.example.RestApi.exception.CSVInvalidDirectoryException;
import com.example.RestApi.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Ping endpoint
     * @return
     */
    @RequestMapping("/ping")
    public @ResponseBody String ping() {
        return "Hello!";
    }

    /**
     * Method accepts customer JSON to save in DB.
     *
     * @param customer
     */
    @PostMapping("/customers")
    public void saveCustomer(@RequestBody Customer customer) {
        customerService.save(customer);
    }

    /**
     * Method to find a customer based on the reference.
     *
     * @param customerRef
     * @return Customer matching reference
     */
    @GetMapping("/customers/{customerRef}")
    public Customer findCustomer(@PathVariable String customerRef) {
        return customerService.findCustomers(customerRef);
    }

    /**
     * Method accepts directory location as input, parses csv files in that location and saves in DB.
     *
     * @param directory
     */
    @PostMapping("/customers/path")
    public void readAndUploadCSVFromPath(@RequestParam String directory) {
        try {

            List<Path> csvFiles = new ArrayList<>();
            Files.newDirectoryStream(Paths.get(directory), path -> path.toString().endsWith(".csv")).forEach(csvFiles::add);
            LOG.info(String.format("Parsing %d CSV files in directory: ", csvFiles.size()) + csvFiles);
            csvFiles.forEach(file -> {
                try {
                    List<Customer> customers = CSVUtility.csvToCustomers(Files.newInputStream(file));
                    customers.forEach(this::saveCustomer);
                    LOG.info(String.format("Inserted %d records successfully!", customers.size()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (IOException e) {
            throw new CSVInvalidDirectoryException("Failed to store CSV data: " + e.getMessage());
        }
    }

}
