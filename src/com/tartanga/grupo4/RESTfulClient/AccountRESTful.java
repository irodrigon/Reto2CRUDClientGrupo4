/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.RESTfulClient;

import com.tartanga.grupo4.businesslogic.AccountLogicController;
import com.tartanga.grupo4.models.Account;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:AccountFacadeREST
 * [com.tartanga.grupo4.accounts.account]<br>
 * USAGE:
 * <pre>
        AccountRESTful client = new AccountRESTful();
        Object response = client.XXX(...);
        // do whatever with response
        client.close();
 </pre>
 *
 * @author Iñi
 */
public class AccountRESTful implements AccountLogicController{

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Reto2CRUDServerGrupo4/webresources";

    public AccountRESTful() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("com.tartanga.grupo4.accounts.account");
    }
    
    @Override
    public List<Account> findAllAccounts(GenericType<List<Account>> responseType) {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }
}
