package com.devfactory.assignment4.repository;

import com.devfactory.assignment4.model.OrderProduct;
import com.devfactory.assignment4.model.OrderProductId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@Repository
public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductId> {
}
