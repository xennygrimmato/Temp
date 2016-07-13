package com.devfactory.assignment4.repository;

import com.devfactory.assignment4.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Customer findUniqueByCompanyName(String companyName);
}
