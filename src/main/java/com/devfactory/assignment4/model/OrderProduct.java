package com.devfactory.assignment4.model;

import com.devfactory.assignment4.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@Entity
@IdClass(OrderProductId.class)
@Table(name="order_product")
public class OrderProduct {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="buying_cost")
    private BigDecimal buyingCost;

    @Column(name="selling_cost")
    private BigDecimal sellingCost;


    public OrderProduct(Integer productId, Integer orderId, Integer quantity, BigDecimal buyingCost, BigDecimal sellingCost) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.buyingCost = buyingCost;
        this.sellingCost = sellingCost;
    }

    public OrderProduct() {
    }

    public BigDecimal getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(BigDecimal sellingCost) {
        this.sellingCost = sellingCost;
    }

    public BigDecimal getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(BigDecimal buyingCost) {
        this.buyingCost = buyingCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
