package com.cella.interview.domain.service;

import com.cella.interview.domain.model.Product;
import com.cella.interview.exception.GenericException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public List<Product> getAll() throws GenericException;

    public Product getById(Long id) throws GenericException;

    public Product getByName(String name) throws GenericException;

    public List<Product> getSortedByNameAscProducts() throws GenericException;

    public List<Product> getSortedByNameDescProducts() throws GenericException;

    public Page<Product> getPaginatedProducts(int page, int size) throws GenericException;

    public Product create(Product product) throws GenericException;

    public Product update(Product product) throws GenericException;

    public void delete(Long id) throws GenericException;


}