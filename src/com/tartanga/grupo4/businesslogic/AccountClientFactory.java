/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.RESTfulClient.AccountRESTful;

/**
 *
 * @author Iñi
 */
public class AccountClientFactory {
     public static AccountLogicController accountLogic(){
        return new AccountRESTful();
    }
}
