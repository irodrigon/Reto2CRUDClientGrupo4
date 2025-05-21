/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;
import com.tartanga.grupo4.RESTfullClient.CustomerRESTfull;
/**
 *
 * @author rabio
 */
public class CustomerFactory {
    private static CustomerFactory instance;
    
    /**
    * Private constructor to prevent external instantiation, 
    * enforcing the singleton pattern.
    */
    private CustomerFactory() {
    }
    /**
    * Retrieves the singleton instance of {@code ClientFactory}. If no instance
    * exists, it creates one.
    *
    * @return the singleton {@code ClientFactory} instance
    */
    public static synchronized CustomerFactory getInstance() {
        if (instance == null) {
            instance = new CustomerFactory();
        }
        return instance;
    }

    /**
    * Provides an instance of {@code Signable}, currently implemented by 
    * the {@code Cliente} class.
    *
    * @return a new instance of {@code Cliente}, cast as {@code Signable}
    */
    public Icustomer getIcustomer() {
        return new CustomerRESTfull();
    }
}
