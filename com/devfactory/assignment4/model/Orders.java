package com.devfactory.assignment4.model;

import com.devfactory.assignment4.controller.ProductController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@Entity
@Table(name="orders")
public class Orders {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="oid")
    private int orderId;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", referencedColumnName = "id")
    public Customer customer;

    @Transient
    @JsonIgnore
    @Column(name="uid", insertable = false, updatable = false)
    private Integer customerId;


    @Column(name="amount")
    private BigDecimal amount;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="timestamp")
    private Date date;

    @Transient
    @JsonProperty("date")
    private String formattedDate;

    @Column(name="status")
    private String status;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormattedDate() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(getDate());
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
