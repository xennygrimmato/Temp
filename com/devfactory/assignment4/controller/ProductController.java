package com.devfactory.assignment4.controller;

/**
 * Created by vaibhavtulsyan on 07/07/16.
 */

import com.devfactory.assignment4.model.Product;
import com.devfactory.assignment4.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    ProductRepository productRepo;

    @RequestMapping("/products")
    public ResponseEntity products() {
        Iterable p = productRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(p);
    }

    @RequestMapping(value="/products/{id}", method=RequestMethod.GET)
    public ResponseEntity<Object> getProduct(@PathVariable int id) {
        Product product = productRepo.findOne(id);

        if(product == null) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        if(product.getDeleted() == 1) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Object>(product, HttpStatus.OK);
    }

    @RequestMapping(value="/products", method=RequestMethod.POST)
    public ResponseEntity<Object> products(@RequestBody Product product) {
        String code = product.getCode();
        if(code == null) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        Product p = productRepo.save(product);
        return new ResponseEntity<Object>(product, HttpStatus.CREATED);
    }

    @RequestMapping(value="/products/{id}", method=RequestMethod.PUT)
    public ResponseEntity putProduct(@PathVariable int id, @RequestBody Product product) {

        Product p = productRepo.findOne(id);

        if(p == null) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        if(p.getDeleted() == 1) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        if(product.getCode() == null) {
            // code is a compulsory field
            // return BAD_REQUEST if it is not part of request body
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        } else {
            p.setCode(product.getCode());
        }

        p.setName(product.getName());
        p.setDescription(product.getDescription());
        p.setDeleted(product.getDeleted());
        p.setRemaining(product.getRemaining());

        productRepo.save(p);
        return new ResponseEntity<Product>(p, HttpStatus.OK);
    }

    @RequestMapping(value="/products/{id}", method=RequestMethod.PATCH)
    public ResponseEntity<Object> patchProduct(@PathVariable int id, @RequestBody Product product) {

        Product p = productRepo.findOne(id);

        if(p == null) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        if(p.getDeleted() == 1) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        if(product.getCode() != null) {
            p.setCode(product.getCode());
        }

        if(product.getName() != null) {
            p.setName(product.getName());
        }

        if(product.getDescription() != null) {
            p.setDescription(product.getDescription());
        }

        if(product.getRemaining() != null) {
            p.setRemaining(product.getRemaining());
        }

        if(product.getDeleted() != null) {
            p.setDeleted(product.getDeleted());
        }

        productRepo.save(p);
        return new ResponseEntity<Object>(p, HttpStatus.OK);

    }

    @RequestMapping(value="/products/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Object> deleteProduct(@PathVariable int id) {

        Product p = productRepo.findOne(id);
        if(p == null) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }
        if(p.getDeleted() == 1) {
            Map<String,String> detailObject = new HashMap<String,String>();
            detailObject.put("detail", "Not found.");
            return new ResponseEntity<Object>(detailObject, HttpStatus.NOT_FOUND);
        }

        p.setDeleted(1);
        productRepo.save(p);
        Map<String, String> empty = new HashMap<String, String>();
        return new ResponseEntity<Object>(empty, HttpStatus.OK);

    }
}
