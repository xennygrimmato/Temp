package com.devfactory.assignment4.repository;

import com.devfactory.assignment4.model.OrderProduct;
import com.devfactory.assignment4.model.OrderProductId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@Repository
public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductId> {

    Iterable<OrderProduct> findByOrderId(Integer orderId);
    Iterable<OrderProduct> findByProductId(Integer productId);

    @Query(value="SELECT op.productId AS productId, op.orderId AS orderId," +
            " op.quantity AS quantity, op.sellingCost AS sellingCost" +
            " FROM OrderProduct AS op WHERE op.orderId = :orderId")
    Iterable<OrderProduct> getByOrderId(@Param("orderId") Integer orderId);


}
