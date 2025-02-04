/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Product;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public interface ProductLogicController {
    List<Product> findAllProducts(GenericType<List<Product>> responseType);
    
    public void linkCustomer(Product requestEntity, String id);
    
    public List<Product> find(GenericType<List<Product>> responseType, String from, String to);
}
