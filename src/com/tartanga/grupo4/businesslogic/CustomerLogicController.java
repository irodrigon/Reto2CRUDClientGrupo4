/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Customer;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public interface CustomerLogicController {
    
    public List<Customer> findAllCustomers(GenericType<List<Customer>> responseType);
}
