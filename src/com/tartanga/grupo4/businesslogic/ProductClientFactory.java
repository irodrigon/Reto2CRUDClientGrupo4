/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.RESTfulClient.ProductRESTful;

/**
 *
 * @author Iñi
 */
public class ProductClientFactory {
    public static ProductLogicController productLogic(){
        return new ProductRESTful();
    }
}
