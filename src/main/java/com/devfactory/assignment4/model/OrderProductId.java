package com.devfactory.assignment4.model;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by vaibhavtulsyan on 13/07/16.
 */

public class OrderProductId implements Serializable {

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "product_id")
    private Integer productId;

    public OrderProductId() {
    }

    public OrderProductId(Integer orderId, Integer productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
