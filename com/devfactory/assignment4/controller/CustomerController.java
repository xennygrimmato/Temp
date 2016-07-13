package com.devfactory.assignment4.controller;

import com.devfactory.assignment4.model.Customer;
import com.devfactory.assignment4.repository.CustomerRepository;
import com.devfactory.assignment4.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@RequestMapping("/api")
@RestController
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    CustomerService customerService;

    @RequestMapping("/customers")
    public ResponseEntity getCustomers() {
        List<Customer> listCustomers = customerService.getAllCustomers();
        return new ResponseEntity(listCustomers, HttpStatus.OK);
    }
}
