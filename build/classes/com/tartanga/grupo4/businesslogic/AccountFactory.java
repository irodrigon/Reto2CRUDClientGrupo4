/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.RESTfullClient.AccountRESTFull;

/**
 *
 * @author 2dami
 */
public class AccountFactory {
    private static AccountFactory instance;
    
    /**
    * Private constructor to prevent external instantiation, 
    * enforcing the singleton pattern.
    */
    private AccountFactory() {
    }
    /**
    * Retrieves the singleton instance of {@code ClientFactory}. If no instance
    * exists, it creates one.
    *
    * @return the singleton {@code ClientFactory} instance
    */
    public static synchronized AccountFactory getInstance() {
        if (instance == null) {
            instance = new AccountFactory();
        }
        return instance;
    }

    /**
    * Provides an instance of {@code Signable}, currently implemented by 
    * the {@code Cliente} class.
    *
    * @return a new instance of {@code Cliente}, cast as {@code Signable}
    */
    public Iaccounts getIaccounts() {
        return new AccountRESTFull();
    }
}
