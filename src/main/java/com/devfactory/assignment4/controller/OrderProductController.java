package com.devfactory.assignment4.controller;

import com.devfactory.assignment4.model.OrderProduct;
import com.devfactory.assignment4.repository.OrderProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@RequestMapping("/api")
@RestController
public class OrderProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    OrderProductRepository orderProductRepo;

    @RequestMapping("/order_products")
    public Iterable<OrderProduct> orderProducts() {
        return orderProductRepo.findAll();
    }
}
