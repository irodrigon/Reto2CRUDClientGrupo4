/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Iñi
 */
public class CreditCardBean {
    
    private Integer iDProduct;
    
    private SimpleStringProperty creditCardNumber;

    private SimpleObjectProperty<LocalDate> creationDate;

    private SimpleObjectProperty<LocalDate> expirationDate;

    private SimpleStringProperty cvv;

    private SimpleStringProperty pin;

    private StringProperty accountNumber;

    public CreditCardBean() {
        this.creditCardNumber = new SimpleStringProperty();
        this.cvv = new SimpleStringProperty();
        this.pin = new SimpleStringProperty();
        this.creationDate = new SimpleObjectProperty<>();
        this.expirationDate = new SimpleObjectProperty<>();
        this.accountNumber = new SimpleStringProperty();
    }

    public CreditCardBean(String creditCardNumber, LocalDate creationDate, LocalDate expirationDate, String cvv, String pin, String accountNumber) {
        this.creditCardNumber = new SimpleStringProperty(creditCardNumber);
        this.creationDate = new SimpleObjectProperty<>(creationDate);
        this.expirationDate = new SimpleObjectProperty<>(expirationDate);
        this.cvv = new SimpleStringProperty(cvv);
        this.pin = new SimpleStringProperty(pin);
        this.accountNumber = new SimpleStringProperty(accountNumber);
    }

    public CreditCardBean(String creditCardNumber, String cvv, String pin, String accountNumber) {
        this.creditCardNumber = new SimpleStringProperty(creditCardNumber);
        this.creationDate = new SimpleObjectProperty<>();
        this.expirationDate = new SimpleObjectProperty<>();
        this.cvv = new SimpleStringProperty(cvv);
        this.pin = new SimpleStringProperty(pin);
        this.accountNumber = new SimpleStringProperty(accountNumber);
    }

    public String getCreditCardNumber() {
        return this.creditCardNumber.get();
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber.set(creditCardNumber);
    }

    public String getCreationDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (this.creationDate.get() == null) {
            return null;
        } else {
            return dateTimeFormatter.format(this.creationDate.get());
        }
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }

    public String getExpirationDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (this.expirationDate.get() == null) {
            return null;
        } else {
            return dateTimeFormatter.format(this.expirationDate.get());
        }
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate.set(expirationDate);
    }

    public String getCvv() {
        return this.cvv.get();
    }

    public void setCvv(String cvv) {
        this.cvv.set(cvv);
    }

    public String getPin() {
        return this.pin.get();
    }

    public void setPin(String pin) {
        this.pin.set(pin);
    }

    public String getAccountNumber() {
        return this.accountNumber.get();
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber.set(accountNumber);
    }

    public Integer getiDProduct() {
        return iDProduct;
    }

    public void setiDProduct(Integer iDProduct) {
        this.iDProduct = iDProduct;
    }

}
