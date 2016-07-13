package com.devfactory.assignment4.service;

import com.devfactory.assignment4.model.Customer;
import com.devfactory.assignment4.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavtulsyan on 12/07/16.
 */
@Component
public class CustomerService {

    Logger LOGGER = LoggerFactory.getLogger(CustomerService.class.getName());

    @Autowired
    CustomerRepository customerRepo;

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<Customer>();
        for(Customer customer : customerRepo.findAll()) {
            customers.add(customer);
        }
        return customers;
    }

    public Customer getCustomer(String customerName) {
        Customer customer = customerRepo.findUniqueByCompanyName(customerName);
        return customer;
    }

    public Customer addUser(String customerName) {
        try {
            Customer customer = new Customer();
            customer.setCompanyName(customerName);
            customerRepo.save(customer);
            return customer;
        } catch(Exception e) {
            // maybe a UserCreationException object must be thrown
            LOGGER.error(e.getMessage());
            Customer customer = new Customer();
            customer.setCompanyName("");
            return customer;
        }
    }
}
