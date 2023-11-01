package com.example.RestApi.csv;

import com.example.RestApi.domain.Customer;
import com.example.RestApi.exception.CSVParseFailureException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtility {
    public static String TYPE = "text/csv";
    static String[] HEADERs = {"Customer Ref", "Customer Name", "Address Line 1", "Address Line 2", "Town", "County", "Country", "Postcode"};

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Customer> csvToCustomers(InputStream is) {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<Customer> customers = new ArrayList<Customer>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Customer customer = new Customer(
                        csvRecord.get("Customer Ref"),
                        csvRecord.get("Customer Name"),
                        csvRecord.get("Address Line 1"),
                        csvRecord.get("Address Line 2"),
                        csvRecord.get("Town"),
                        csvRecord.get("County"),
                        csvRecord.get("Country"),
                        csvRecord.get("Postcode"));

                customers.add(customer);
            }

            return customers;
        } catch (Exception e) {
            throw new CSVParseFailureException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
