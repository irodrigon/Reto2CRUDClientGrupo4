/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author 2dami
 */
public class AccountBean {
    
        private final SimpleStringProperty accountNumber;
        private final SimpleStringProperty name;
        private final SimpleStringProperty surname;
        private final SimpleStringProperty creationDate;
        private final SimpleDoubleProperty balance;

        public AccountBean(String accountNumber, String name, String surname, String creationDate, Double balance) {
            
            this.accountNumber = new SimpleStringProperty(accountNumber);
            this.name = new SimpleStringProperty(name);
            this.surname = new SimpleStringProperty(surname);
            this.creationDate = new SimpleStringProperty(creationDate);
            this.balance = new SimpleDoubleProperty(balance);
        }

        public String getAccountNumber(){
            return this.accountNumber.get();
        }
        
        public void setAccountNumber(String accountNumber){
            this.accountNumber.set(accountNumber);
        }
        
        public String getName(){
            return this.name.get();
        }
        
        public void setName(String name){
            this.name.set(name);
        }
        
        public String getSurname(){
            return this.surname.get();
        }
        
        public void setSurname(String surname){
            this.surname.set(surname);
        }
        
        public String getCreationDate(){
            return this.creationDate.get();
        }
        
        public void setCreationDate(String creationDate){
            this.creationDate.set(creationDate);
        }
        
        public Double getBalance(){
            return this.balance.get();
        }
        
        public void setBalance(Double balance){
            this.balance.set(balance);
        }
        
        
        


    
}
