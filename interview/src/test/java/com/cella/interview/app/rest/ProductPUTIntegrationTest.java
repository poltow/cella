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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductPUTIntegrationTest {

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

        // Insert one product
        ResponseEntity<String> result = restTemplate.postForEntity(new URI(url),
                new HttpEntity<>(new Product(-1L, "PRODUCT_NAME", "PRODUCT_DESC", 1.2d, 3.4d, "USA")), String.class);
        assertEquals(201, result.getStatusCodeValue());
        Product product = objectMapper.readValue(result.getBody(), Product.class);

        // Name is mandatory
        product.setName("");
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());

        product.setName(null);
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());
        product.setName("NAME");

        // Description is mandatory
        product.setDescription(null);
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());

        product.setDescription("");
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());
        product.setDescription("DESC");

        // Weight should be positive or zero
        product.setWeight(-1d);
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());
        product.setWeight(20d);

        // Price should be positive
        product.setPrice(-1d);
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());

        product.setPrice(0d);
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());
        product.setPrice(20d);

        // Country should be valid
        product.setCountry("XXX");
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());

        product.setCountry(null);
        assertEquals(400, restTemplate.exchange(new URI(url + product.getId()), HttpMethod.PUT,
                new HttpEntity<>(product), Product.class).getStatusCodeValue());

        //clean DB
        restTemplate.delete(new URI(url + product.getId()));

        //assert is clean
        productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 0);
    }
}