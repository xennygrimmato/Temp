package com.devfactory.assignment4.model;

import com.devfactory.assignment4.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by vaibhavtulsyan on 09/07/16.
 */

@Entity
@Table(name="user")
public class Customer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="first_name")
    private String firstName = "";

    @Column(name="last_name")
    private String lastName = "";

    @Column(name="company_name")
    private String companyName = "";

    @Column(name="email")
    private String email = "";

    @Column(name="phone")
    private String phone;

    @Column(name="addr_line1")
    private String addrLine1;

    @Column(name="addr_line2")
    private String addrLine2;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="postal_code")
    private String postalCode;

    @Column(name="country")
    private String country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddrLine1() {
        return addrLine1;
    }

    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    public String getAddrLine2() {
        return addrLine2;
    }

    public void setAddrLine2(String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
