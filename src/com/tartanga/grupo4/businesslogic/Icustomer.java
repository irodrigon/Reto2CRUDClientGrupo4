/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.exception.ReadException;
import com.tartanga.grupo4.models.Customer;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * Interface for customer-related operations.
 * This interface defines methods for retrieving customer data.
 * 
 * @author rabio
 */
public interface Icustomer {

    /**
     * Retrieves a list of all customers in XML format.
     *
     * @param responseType The type of response expected.
     * @return A list of {@link Customer} objects.
     * @throws WebApplicationException if an error occurs while retrieving the customers.
     */
    public List<Customer> findAll_XML(GenericType<List<Customer>> responseType) throws WebApplicationException;
}