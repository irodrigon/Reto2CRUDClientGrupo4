/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;


import com.tartanga.grupo4.exception.ReadException;
import com.tartanga.grupo4.models.Account;
import com.tartanga.grupo4.models.Customer;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dami
 */
public interface Iaccounts {
    public List<Account> getAllAccounts(GenericType<List<Account>> responseType) throws WebApplicationException;
    public List<Customer> findByName(GenericType<List<Customer>> responseType, String name) throws WebApplicationException; 
    public List<Customer> findByNameSurname(GenericType<List<Customer>> responseType, String name, String surname) throws WebApplicationException; 
    public List<Customer> findBySurname(GenericType<List<Customer>> responseType, String surname) throws WebApplicationException; 
    public List<Account> findByDates(GenericType<List<Account>> responseType, String startDate, String endDate) throws WebApplicationException; 
    public Account findByAccount(GenericType<Account> responseType, String accountNumber) throws WebApplicationException; 
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;
    public void remove(String id) throws WebApplicationException;
}
