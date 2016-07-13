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
            order.setCustomer(customer);
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
            // make appropriate changes in inventory
            // add order-product
            // if orderProduct exists, increase quantity ordered
            // else create new
            OrderProductId orderProductId = new OrderProductId();
            orderProductId.setOrderId(order.getOrderId());
            orderProductId.setProductId(product.getId());

            // Get all products associated with a certain Order ID
            Iterable<OrderProduct> orderProducts = orderProductRepo.getByOrderId(orderId);

            OrderProduct existing = null;

            for (OrderProduct orderProduct : orderProducts) {
                if (orderProduct.getProductId() == productId) {
                    existing = orderProduct;
                    break;
                }
            }

            if (existing != null) {
                // increase quantity
                LOGGER.debug("(OrderService) [addItem] : (order_id, product_id) pair already exists. Increasing quantity");
                existing.setQuantity(existing.getQuantity() + quantity);
                orderProductRepo.save(existing);
                return new ResponseEntity(null, HttpStatus.OK);
            } else {
                // save to repo
                OrderProduct newOrderProduct = new OrderProduct();
                newOrderProduct.setOrderId(orderId);
                newOrderProduct.setProductId(productId);
                newOrderProduct.setQuantity(quantity);
                orderProductRepo.save(newOrderProduct);
                return new ResponseEntity(null, HttpStatus.OK);
            }
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }
}
