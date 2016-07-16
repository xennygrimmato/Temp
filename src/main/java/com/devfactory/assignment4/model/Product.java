package com.devfactory.assignment4.model;

import com.devfactory.assignment4.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by vaibhavtulsyan on 07/07/16.
 */


@Entity
@Table(name="product")
public class Product {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String name = "";
    private String code;
    private Integer remaining = 0;
    private String description = "";
    private Integer deleted = 0;

    public Product() {}

    public Product(int id, String name, String code, Integer remaining, String description, Integer deleted) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.remaining = remaining;
        this.description = description;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
