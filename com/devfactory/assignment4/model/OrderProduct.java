package com.devfactory.assignment4.model;

import com.devfactory.assignment4.controller.ProductController;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@Entity
@IdClass(OrderProductId.class)
@Table(name="order_product")
public class OrderProduct implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Transient
    @JsonManagedReference
    @ManyToOne(fetch=FetchType.LAZY)
    public Orders orders;

    @Transient
    @JsonManagedReference
    @ManyToOne(fetch=FetchType.LAZY)
    public Product product;

    @Id
    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer productId;

    @Id
    @Column(name = "order_id", insertable = false, updatable = false)
    private Integer orderId;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="buying_cost")
    private BigDecimal buyingCost;

    @Column(name="selling_cost")
    private BigDecimal sellingCost;

    public OrderProduct(Orders orders, Product product, Integer productId, Integer orderId, Integer quantity, BigDecimal buyingCost, BigDecimal sellingCost) {
        this.orders = orders;
        this.product = product;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.buyingCost = buyingCost;
        this.sellingCost = sellingCost;
    }

    public OrderProduct() {}

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBuyingCost() {
        return buyingCost;
    }

    public void setBuyingCost(BigDecimal buyingCost) {
        this.buyingCost = buyingCost;
    }

    public BigDecimal getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(BigDecimal sellingCost) {
        this.sellingCost = sellingCost;
    }
}
