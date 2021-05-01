package com.cella.interview.app.rest;

import com.cella.interview.domain.model.Product;
import com.cella.interview.domain.service.ProductService;
import com.cella.interview.exception.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.cella.interview.validators.CountryCodeValidator.isValid;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //GET
    @GetMapping(path = "/")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Product>> findAll() throws GenericException {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) throws GenericException {
        return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/name/{name}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Product> findByName(@PathVariable("name") String name) throws GenericException {
        return new ResponseEntity<>(productService.getByName(name), HttpStatus.OK);
    }

    @GetMapping(path = "/", params = {"page", "size"})
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Product>> findPaginated(@RequestParam("page") int page,
                                                       @RequestParam("size") int size) throws GenericException {
        return new ResponseEntity<>(productService.getPaginatedProducts(page, size).getContent(), HttpStatus.OK);
    }

    @GetMapping(path = "/", params = {"sortOrder"})
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Product>> findSorted(@RequestParam("sortOrder") String sortOrder) throws GenericException {
        switch (sortOrder.toUpperCase()) {
            case "ASC":
                return new ResponseEntity<>(productService.getSortedByNameAscProducts(), HttpStatus.OK);
            case "DESC":
                return new ResponseEntity<>(productService.getSortedByNameDescProducts(), HttpStatus.OK);
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Provide correct sortOrder value [ASC/DESC]: " + sortOrder);
        }
    }

    //POST
    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Product> save(@Valid @NotNull @RequestBody Product product) throws GenericException {
        validateCountryCode(product.getCountry());
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    //PUT
    @PutMapping(path = "{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Product> update(@PathVariable("id") Long id, @Valid @NotNull @RequestBody Product product)
            throws GenericException {
        validateCountryCode(product.getCountry());
        product.setId(id);
        return new ResponseEntity<>(productService.update(product), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Product> remove(@PathVariable("id") Long id) throws GenericException {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateCountryCode(String countryCode) {
        if (!isValid(countryCode))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Provide correct Country code value [ISO 3166 - ALPHA 3]: " + countryCode);
    }
}