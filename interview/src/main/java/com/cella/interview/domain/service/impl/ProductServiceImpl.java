package com.cella.interview.domain.service.impl;

import com.cella.interview.domain.model.Product;
import com.cella.interview.domain.repository.ProductRepository;
import com.cella.interview.domain.service.ProductService;
import com.cella.interview.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product create(Product product) throws GenericException {
        logger.info("Creating the product {}", product.toString());

        Product newProduct = productRepository.save(product);
        validateSaveProductResult(newProduct, String.format("Error during creating the product %s", product));

        logger.info("Product {} successfully created", newProduct.getId());
        return newProduct;
    }

    @Override
    public Product update(Product product) throws GenericException {
        logger.info("Updating the product {}", product.toString());

        validateProductExists(product.getId());
        Product updatedProduct = productRepository.save(product);
        validateSaveProductResult(updatedProduct, String.format("Error during updating the product %s", product));

        logger.info("Product {} successfully updated", product.getId());
        return updatedProduct;
    }

    private void validateSaveProductResult(Product product, String msg) throws GenericException {
        if (product == null) {
            logger.error(msg);
            throw new EntityNotSavedException(msg);
        }
    }

    @Override
    public void delete(Long id) throws GenericException {
        logger.info("Deleging the product {}", id);

        validateProductExists(id);

        try {
            productRepository.deleteById(id);
            logger.info("Product {} successfully deleted", id);
        } catch (Exception e) {
            String msg = String.format("Error during deleting the product %s \n %s", id, e.getMessage());
            logger.error(msg);
            throw new EntityNotDeletedException(msg);
        }
    }

    private void validateProductExists(Long id) throws GenericException {
        if (!productRepository.existsById(id)) {
            String msg = String.format("Can't find the product %s", id);
            logger.error(msg);
            throw new EntityNotFoundException(msg);
        }
    }

    @Override
    public List<Product> getAll() {
        logger.info("Retrieving all products");

        List<Product> result = (List<Product>) productRepository.findAll();

        logger.info("All products result: " + result.toString());
        return result;
    }

    @Override
    public Product getById(Long id) throws GenericException {
        logger.info("Retrieving product {}", id);

        Optional<Product> product = productRepository.findById(id);
        validateGetProductResult(String.valueOf(id), product);

        logger.info("Returning product: " + product.orElseThrow().toString());
        return product.orElseThrow();
    }

    @Override
    public Product getByName(String name) throws GenericException {
        logger.info("Retrieving product {}", name);

        Optional<Product> product = productRepository.getByName(name);
        validateGetProductResult(name, product);

        logger.info("Returning product: " + product.orElseThrow().toString());
        return product.orElseThrow();
    }

    @Override
    public List<Product> getSortedByNameAscProducts() throws GenericException {
        logger.info("Retrieving products sorted by name Asc");

        List<Product> result = (List<Product>) productRepository.findAll(Sort.by("name").ascending());

        logger.info("Sorted products result: " + result.toString());
        return result;
    }

    @Override
    public List<Product> getSortedByNameDescProducts() throws GenericException {
        logger.info("Retrieving products sorted by name Desc");

        List<Product> result = (List<Product>) productRepository.findAll(Sort.by("name").descending());

        logger.info("Sorted products result: " + result.toString());
        return result;
    }


    @Override
    public Page<Product> getPaginatedProducts(int page, int size) throws GenericException {
        logger.info("Retrieving {} products from page {}", size, page);

        Page<Product> result = productRepository.findAll(PageRequest.of(page, size));

        if (page > result.getTotalPages()) {
            String msg = String.format("Error retrieving %s products from page %s", size, page);
            logger.error(msg);
            throw new EntityNotFoundException(msg);
        }

        logger.info("Paginated products result: " + result.toString());
        return result;
    }

    private void validateGetProductResult(String productValue, Optional<Product> product) throws EntityNotFoundException {
        if (!product.isPresent()) {
            String msg = String.format("Error retrieving the product %s", productValue);
            logger.error(msg);
            throw new EntityNotFoundException(msg);
        }
    }

}