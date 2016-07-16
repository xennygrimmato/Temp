package com.devfactory.assignment4.service;

import com.devfactory.assignment4.controller.ProductController;
import com.devfactory.assignment4.model.*;
import com.devfactory.assignment4.repository.CustomerRepository;
import com.devfactory.assignment4.repository.OrderProductRepository;
import com.devfactory.assignment4.repository.OrdersRepository;
import com.devfactory.assignment4.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vaibhavtulsyan on 12/07/16.
 */

@Component
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    OrdersRepository ordersRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    OrderProductRepository orderProductRepo;

    public List<Orders> getAllOrders() {
        List<Orders> ordersList = new ArrayList<Orders>();
        for(Orders order : ordersRepo.findAll()) {
            ordersList.add(order);
        }
        return ordersList;
    }

    public Boolean createNewOrder(String customerName) {

        // Get customer who is placing order
        try {
            Customer customer = customerRepo.findUniqueByCompanyName(customerName);
            if (customer == null) {

                Customer userAdded = customerService.addUser(customerName);
                // Retrieving  the user that was added in the repo
                // Note: This user object has a newly assigned auto-incremented ID

                customer = customerRepo.findUniqueByCompanyName(customerName);

            } else {
                // do nothing
                // user exists
            }

            Date date = new Date();
            Orders order = new Orders();
            order.setDate(date);
            order.setCustomerId(customer.getId());
            order.setStatus("Created");
            order.setAmount(new BigDecimal(0));
            ordersRepo.save(order);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public ResponseEntity addItem(Integer productId, Integer quantity, Integer orderId) {

        try {
            // product must exist
            // quantity must be <= remaining stock of product
            Product product = productRepo.findOne(productId);
            if (product == null) {
                LOGGER.debug("(OrderService) [addItem] : Product with" + productId + "not found");
                return new ResponseEntity(null, HttpStatus.NOT_FOUND);
            }
            //Will check for quantity during submit
            //Integer remainingStock = product.getRemaining();
            //if (remainingStock < quantity) {
            //    return false;
            //}
            Orders order = ordersRepo.findOne(orderId);
            if (order == null) {
                return new ResponseEntity(null, HttpStatus.NOT_FOUND);
            }

            LOGGER.debug("Creating OrderProduct instance");
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(orderId);
            orderProduct.setProductId(productId);
            orderProduct.setSellingCost(new BigDecimal(0));
            LOGGER.debug("Completed creating OrderProduct instance");

            // make appropriate changes in inventory
            // add order-product
            // if orderProduct exists, increase quantity ordered
            // else create new
            LOGGER.debug("Creating OrderProductId instance");
            OrderProductId orderProductId = new OrderProductId();
            LOGGER.debug("Setting OrderId");
            orderProductId.setOrderId(order.getOrderId());
            LOGGER.debug("Setting ProductId");
            orderProductId.setProductId(product.getId());

            LOGGER.debug("Finding existing OrderProduct");
            OrderProduct existing = orderProductRepo.findOne(orderProductId);
            LOGGER.debug("Completed finding existing OrderProduct");

            if (existing != null) {
                // increase quantity
                LOGGER.debug("(OrderService) [addItem] : (order_id, product_id) pair already exists. Increasing quantity");

                LOGGER.debug("Setting quantity");

                Integer existingQuantity = existing.getQuantity();
                Integer newQuantity = existingQuantity + quantity;

                if(newQuantity < 0) newQuantity = 0;

                existing.setQuantity(newQuantity);

                LOGGER.debug("Saving to OrderProductRepository");
                orderProductRepo.save(existing);
                return new ResponseEntity(null, HttpStatus.CREATED);
            } else {
                // save to repo

                LOGGER.debug("Creating new OrderProduct object - does not already exist in table.");
                OrderProduct newOrderProduct = new OrderProduct();

                newOrderProduct.setOrderId(orderId);
                newOrderProduct.setProductId(productId);
                quantity = ((quantity < 0) ? 0 : quantity);
                newOrderProduct.setQuantity(quantity);

                LOGGER.debug("Saving new OrderProduct instance to repo");
                orderProductRepo.save(newOrderProduct);
                return new ResponseEntity(null, HttpStatus.CREATED);
            }
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity performSubmit(String address, String customerName, Integer orderId) {

        try {
            Orders order = ordersRepo.findOne(orderId);

            if (order == null) {
                return new ResponseEntity(null, HttpStatus.NOT_FOUND);
            }

            LOGGER.debug("(OrderService) [performSubmit] : Performing Submit.");
            LOGGER.debug("(OrderService) [performSubmit] : Getting all products of order.");
            LOGGER.debug("(OrderService) [performSubmit] : Completed getting all products of order.");

            Boolean cancelled = false;

            for (OrderProduct orderProduct : order.getOrderToProductMap()) {
                Product _product_ = productRepo.findOne(orderProduct.getProductId());

                Integer remaining = _product_.getRemaining();

                if(orderId == orderProduct.getOrderId()) {
                    Integer quantityOrdered = orderProduct.getQuantity();
                    if (quantityOrdered > remaining) {
                        LOGGER.debug("(OrderService) [performSubmit] : Cancelling Order");
                        // cancel order
                        cancelled = true;
                        // change status
                        order.setStatus("Cancelled");
                        ordersRepo.save(order);
                        LOGGER.debug("(OrderService) [performSubmit] : Completed cancelling order");
                        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
                    }
                }
            }

            if (!cancelled) {
                LOGGER.debug("(OrderService) [performSubmit] : Updating inventory");
                for (OrderProduct orderProduct : order.getOrderToProductMap()) {
                    if(orderProduct == null) {
                        LOGGER.debug("(OrderService) [performSubmit] : orderProduct is null. Continuing.");
                    }
                    Product _product_ = productRepo.findOne(orderProduct.getProductId());
                    Integer remaining = _product_.getRemaining();
                    LOGGER.debug(remaining.toString());
                    Integer quantityOrdered = orderProduct.getQuantity();
                    LOGGER.debug(quantityOrdered.toString());
                    int newRemaining = remaining - quantityOrdered; // null
                    if (newRemaining < 0) {
                        newRemaining = 0;
                    }
                    _product_.setRemaining(newRemaining);
                    productRepo.save(_product_);
                }
                LOGGER.debug("(OrderService) [performSubmit] : Completed updating inventory");
            }

            // 1. if customer name is given:
            //        - check if customer with this name already exists. if yes, set user to this customer
            //        - otherwise, create new customer and then assign
            //        - update status

            if (customerName != null) {
                LOGGER.debug("(OrderService) [performSubmit] : Customer Name is provided.");
                Customer customer = customerRepo.findUniqueByCompanyName(customerName);
                if (customer != null) {
                    LOGGER.debug("(OrderService) [performSubmit] : Customer exists.");
                    // TODO:
                    // addrline1 has max limit 512
                    // if length of address > 512, split into 2
                    // assign 2nd part to addrline2
                    // if length > 1024, truncate address or throw an exception (latter would be better ofcourse)
                    customer.setAddrLine1(address);

                    order.setStatus("checkout");
                    ordersRepo.save(order);
                    LOGGER.debug("(OrderService) [performSubmit] : Completed order submit from existing customer");
                } else {
                    LOGGER.debug("(OrderService) [performSubmit] : New Customer ordering");
                    customer = new Customer();
                    customer.setCompanyName(customerName);
                    customer.setAddrLine1(address);
                    customerRepo.save(customer);
                    customer = customerRepo.findUniqueByCompanyName(customerName);

                    order.setStatus("checkout");
                    ordersRepo.save(order);
                    LOGGER.debug("(OrderService) [performSubmit] : Completed order submit from new customer");
                }
                return new ResponseEntity(null, HttpStatus.OK);
            }

            // 2. customer name not given
            //        - change status to checkout
            else {
                order.setStatus("checkout");
                ordersRepo.save(order);
                return new ResponseEntity(null, HttpStatus.OK);
            }
        } catch(Exception e) {
            LOGGER.debug(e.getMessage());
            LOGGER.debug("(OrderService) [performSubmit] : Exception caught.");
            e.printStackTrace();
        }
        LOGGER.debug("(OrderService) [performSubmit] : Returning from performSubmit.");
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }
}
