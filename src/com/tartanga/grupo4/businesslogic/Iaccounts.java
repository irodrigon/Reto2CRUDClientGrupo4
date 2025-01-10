/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.controller.AccountBean;
import com.tartanga.grupo4.exception.ReadException;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dami
 */
public interface Iaccounts {
    public List<AccountBean> findAll_XML(GenericType<List<AccountBean>> responseType) throws ClientErrorException, ReadException;
    public List<AccountBean> findByName(GenericType<List<AccountBean>> responseType, String name) throws  ClientErrorException, ReadException; 
    public List<AccountBean> findByNameSurname(GenericType<List<AccountBean>> responseType, String name, String surname) throws ClientErrorException, ReadException; 
    public List<AccountBean> findBySurname(GenericType<List<AccountBean>> responseType, String surname) throws ClientErrorException, ReadException; 
    public List<AccountBean> findByDates(GenericType<List<AccountBean>> responseType, String startDate, String endDate) throws ClientErrorException, ReadException; 
    public List<AccountBean> findByAccount(GenericType<List<AccountBean>> responseType, String accountNumber) throws ClientErrorException, ReadException; 

}
