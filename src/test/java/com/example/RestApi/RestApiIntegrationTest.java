package com.example.RestApi;

import com.example.RestApi.domain.Customer;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiIntegrationTest {

    private static final String VALID_REFERENCE_ID = "1";
    private static final String INVALID_REFERENCE_ID = "-100012";
    private static final String VALID_DIRECTORY = "./csvDirectory/validDirectory";
    private static final String INVALID_DIRECTORY = "/invalid/directory/path";
    private static final String INVALID_CSV_FILE = "./csvDirectory/invalidDirectory";

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @DisplayName("Test Ping endpoint - No business logic")
    @Test
    public void testPing() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/ping"),
                HttpMethod.GET, entity, String.class);

        Assertions.assertEquals("Hello!", response.getBody());
    }

    @DisplayName("Test findCustomer when valid reference is provided should return customer")
    @Test
    public void testFindCustomers_whenValidReferencePassed_shouldReturnCustomer() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Customer> response = restTemplate.exchange(
                createURLWithPort("/customers/" + VALID_REFERENCE_ID),
                HttpMethod.GET, entity, Customer.class);
        Customer expected = new Customer(VALID_REFERENCE_ID, "John", "9", "Ticken road", "borad street", "Rainham", "UK", "W1T 6GH");

        assertTrue(new ReflectionEquals(expected).matches(response.getBody()));
    }

    @DisplayName("Test findCustomer when invalid reference is provided should throw exception")
    @Test
    public void testFindCustomers_whenInvalidReferencePassed_shouldThrowException() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/customers/" + INVALID_REFERENCE_ID),
                HttpMethod.GET, entity, String.class);

        Assertions.assertEquals(String.format("Customer with reference %s not found!", INVALID_REFERENCE_ID), response.getBody());
    }

    @DisplayName("Test saveCustomer when valid path is provided should save successfully")
    @Test
    public void testSaveCustomers_whenValidPathGiven_shouldSaveSuccessfully() throws JSONException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/customers/path"))
                .queryParam("directory", VALID_DIRECTORY);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Customer> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST, entity, Customer.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNull(response.getBody());
    }

    @DisplayName("Test saveCustomer when invalid path is provided should throw exception")
    @Test
    public void testSaveCustomers_whenInvalidPathGiven_shouldThrowException() throws JSONException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/customers/path"))
                .queryParam("directory", INVALID_DIRECTORY);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(), "Failed to store CSV data: " + INVALID_DIRECTORY);
    }

    @DisplayName("Test saveCustomer when invalid CSV is provided should throw exception")
    @Test
    public void testSaveCustomers_whenInvalidCSVGiven_shouldThrowException() throws JSONException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/customers/path"))
                .queryParam("directory", INVALID_CSV_FILE);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST, entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(response.getBody().contains("Failed to parse CSV file"));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}

