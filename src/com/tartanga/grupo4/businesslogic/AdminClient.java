/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Admin;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:AdminFacadeREST
 * [com.tartanga.grupo4.customers.admin]<br>
 * USAGE:
 * <pre>
 *        AdminClient client = new AdminClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Iñi
 */
public class AdminClient implements AdminLogicController{

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Reto2CRUDServerGrupo4/webresources";

    public AdminClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("com.tartanga.grupo4.customers.admin");
    }
    
    @Override
    public Admin findAdminByCredentials(GenericType<Admin> responseType, String logIn, String password) throws WebApplicationException{
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("credentials/{0}/{1}", new Object[]{logIn, password}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public Admin countAdminByLogIn(GenericType<Admin> responseType, String logIn) throws WebApplicationException{
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("credentials/{0}", new Object[]{logIn}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void createAdmin(Admin requestEntity) throws WebApplicationException{
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void close() {
        client.close();
    }
}
