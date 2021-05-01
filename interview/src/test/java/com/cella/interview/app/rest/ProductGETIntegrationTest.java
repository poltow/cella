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

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductGETIntegrationTest {

    public static final String NAME = "NAME_";
    public static final String PRODUCT = "PRODUCT_";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() throws URISyntaxException {

        String url = "http://localhost:" + port + "/products/";
        ObjectMapper objectMapper = new ObjectMapper();

        int prodQty = 6;

        // Product table is empty
        ResponseEntity<Product[]> productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 0);

        // Post products
        for (int i = prodQty / 2; i < prodQty; i++) {
            restTemplate.postForEntity(new URI(url), new HttpEntity<>(createProduct(i)), String.class);
        }
        for (int i = 1; i < prodQty / 2; i++) {
            restTemplate.postForEntity(new URI(url), new HttpEntity<>(createProduct(i)), String.class);
        }
        productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == prodQty - 1);

        //Get by name
        String str = url + "name/" + NAME + "1";
        ResponseEntity<Product> product = restTemplate.getForEntity(new URI(str), Product.class);
        assertEquals(PRODUCT + "1", product.getBody().getDescription());

        //Get paginated products
        productsResponse = restTemplate.getForEntity(new URI(url + "?page=0&size=" + prodQty / 2), Product[].class);
        assertTrue(productsResponse.getBody().length == prodQty / 2);
        Product[] products = productsResponse.getBody();
        for (int i = 0; i < products.length; i++) {
            assertEquals(NAME + "" + (prodQty / 2 + i), products[i].getName());
        }

        productsResponse = restTemplate.getForEntity(new URI(url + "?page=1&size=" + prodQty / 2), Product[].class);
        assertTrue(productsResponse.getBody().length <= prodQty / 2);
        products = productsResponse.getBody();
        for (int i = 0; i < products.length; i++) {
            assertEquals(NAME + "" + (i + 1), products[i].getName());
        }

        //Get sort products
        productsResponse = restTemplate.getForEntity(new URI(url + "?sortOrder=ASC"), Product[].class);
        products = productsResponse.getBody();
        for (int i = 0; i < products.length; i++) {
            assertEquals(NAME + "" + (i + 1), products[i].getName());
        }

        productsResponse = restTemplate.getForEntity(new URI(url + "?sortOrder=DESC"), Product[].class);
        products = productsResponse.getBody();
        for (int i = 0; i < products.length; i++) {
            int j = products.length - i;
            assertEquals(NAME + "" + (j), products[i].getName());
        }

        assertEquals(400, restTemplate.getForEntity(new URI(url + "?sortOrder=XXX"), Product.class).getStatusCodeValue());

        //clean DB
        for (int i = 0; i < products.length; i++) {
            restTemplate.delete(new URI(url + products[i].getId()));
        }

        //assert is clean
        productsResponse = restTemplate.getForEntity(new URI(url), Product[].class);
        assertTrue(productsResponse.getBody().length == 0);
    }

    private Product createProduct(int i) {
        return new Product(-1L, NAME + i, PRODUCT + i,
                i * 1.2d, i * 3.4d, "USA");
    }
}