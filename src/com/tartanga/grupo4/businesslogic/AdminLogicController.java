/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Admin;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public interface AdminLogicController {
    
    public Admin findAdminByCredentials(GenericType<Admin> responseType, String logIn, String password);
    
    public Admin countAdminByLogIn(GenericType<Admin> responseType, String logIn);
    
    public void createAdmin(Admin requestEntity);
    
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;
    
    public Admin getAdmin(GenericType<Admin> responseType, String id) throws WebApplicationException;
    
}
