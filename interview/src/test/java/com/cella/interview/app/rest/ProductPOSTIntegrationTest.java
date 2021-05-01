package com.cella.interview.app.rest;


import com.cella.interview.Application;
import com.cella.interview.domain.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductPOSTIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() throws URISyntaxException, IOException {

        String url = "http://localhost:" + port + "/products/";
        ObjectMapper objectMapper = new ObjectMapper();

        // Product table is empty
        ResponseEntity<Product[]> productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 0);

        // Name is mandatory
        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "", "PRODUCT_DESC", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, null, "PRODUCT_DESC", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        // Description is mandatory
        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", null, 1.2d, 3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        // Weight should be positive or zero
        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "DESC", -1.2d, 3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        // Price should be positive
        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "DESC", 3.4d, 0d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "DESC", 1.2d, -3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        // Country should be valid
        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "DESC", 1.2d, 3.4d, "XXX")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "DESC", 1.2d, 3.4d, null)), String.class);
        assertEquals(400, result.getStatusCodeValue());

        // Name is unique
        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "DESC", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(201, result.getStatusCodeValue());
        Long id = objectMapper.readValue(result.getBody(), Product.class).getId();
        result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "NAME", "", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(400, result.getStatusCodeValue());

        //clean DB
        restTemplate.delete(new URI(url + id));

        //assert is clean
        productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 0);
    }
}