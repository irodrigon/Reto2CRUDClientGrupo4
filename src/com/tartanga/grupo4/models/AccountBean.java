/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;

import java.time.LocalDate;


/**
 *
 * @author 2dami
 */
public class AccountBean {
    
        private Long accountNumber;
        private String name;
        private String surname;
        private String creationDate;
        private Double balance;

        public AccountBean(Long accountNumber, String name, String surname, String creationDate, Double balance) {
            
            this.accountNumber = accountNumber;
            this.name =name;
            this.surname =surname;
            this.creationDate = creationDate;
            this.balance = balance;
        }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

       
        
        
        


    
}