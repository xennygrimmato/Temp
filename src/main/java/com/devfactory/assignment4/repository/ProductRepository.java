package com.devfactory.assignment4.repository;

/**
 * Created by vaibhavtulsyan on 07/07/16.
 */

import com.devfactory.assignment4.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    List<Product> findAllByName(String name);

}
