/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.RESTfulClient.AdminRESTful;

/**
 *
 * @author Iñi
 */
public class AdminClientFactory {
    
    public static AdminLogicController adminLogic(){
        return new AdminRESTful();
    }
    
}
