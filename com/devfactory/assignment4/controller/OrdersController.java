package com.devfactory.assignment4.controller;

import com.devfactory.assignment4.model.Orders;
import com.devfactory.assignment4.repository.OrdersRepository;
import com.devfactory.assignment4.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@RequestMapping("/api")
@RestController
public class OrdersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    OrdersRepository ordersRepo;

    @Autowired
    OrderService orderService;

    @RequestMapping("/orders")
    public ResponseEntity getOrders() {
        List<Orders> allOrders= orderService.getAllOrders();
        return new ResponseEntity(allOrders, HttpStatus.OK);
    }

    // Create an order
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity createOrder(@RequestBody Map<String, Object> requestBody) {

        try {
            String customerName;
            String userName = requestBody.get("user_name").toString();
            if (StringUtils.isBlank(userName)) {
                customerName = "";
            } else {
                customerName = userName;
            }

            Boolean orderAdded = orderService.createNewOrder(customerName);
            if (orderAdded) {
                return new ResponseEntity(null, HttpStatus.OK);
            } else {
                // order was not added
                return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);

    }

    // Add item to order
    @RequestMapping(value = "/orders/{id}/orderLineItem", method = RequestMethod.POST)
    public ResponseEntity addItemToOrder(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        Integer productId = Integer.parseInt(requestBody.get("product_id").toString());
        Integer qty = Integer.parseInt(requestBody.get("qty").toString());
        Orders order = ordersRepo.findOne(id);

        if(productId == null) {
            LOGGER.debug("HTTP/1.1 [POST] /orders/{id}/orderLineItem : product_id not passed in request body");
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        if(requestBody.get("qty") == null) {
            LOGGER.debug("HTTP/1.1 [POST] /orders/{id}/orderLineItem : qty not passed in request body");
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        if(order == null) {
            LOGGER.debug("HTTP/1.1 [POST] /orders/{id}/orderLineItem : order->{id} does not exist in orders");
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return orderService.addItem(productId, qty, id);
    }
}
