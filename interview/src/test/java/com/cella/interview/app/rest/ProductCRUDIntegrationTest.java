package com.cella.interview.app.rest;


import com.cella.interview.Application;
import com.cella.interview.domain.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductCRUDIntegrationTest {

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

        // POST
        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "PRODUCT_NAME", "PRODUCT_DESC", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(201, result.getStatusCodeValue());
        Product firstProduct = objectMapper.readValue(result.getBody(), Product.class);
        assertNotNull(firstProduct.getId());
        Assertions.assertEquals("PRODUCT_NAME", firstProduct.getName());

        // GET ALL
        productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 1);

        // PUT
        firstProduct.setName("UPDATED_NAME");
        assertEquals(200, restTemplate.exchange(new URI(url + firstProduct.getId()), HttpMethod.PUT,
                new HttpEntity<>(firstProduct), Product.class).getStatusCodeValue());

        //GET BY ID
        ResponseEntity<Product> responseProduct = restTemplate.getForEntity(new URI(url + firstProduct.getId()),
                Product.class);
        Assertions.assertEquals("UPDATED_NAME", responseProduct.getBody().getName());

        // DELETE
        restTemplate.delete(new URI(url + firstProduct.getId()));
        productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 0);
    }
}