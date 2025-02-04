/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Product;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:ProductFacadeREST
 * [com.tartanga.grupo4.product.product]<br>
 * USAGE:
 * <pre>
 *        ProductClient client = new ProductClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Iñi
 */
public class ProductClient implements ProductLogicController{

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Reto2CRUDServerGrupo4/webresources";

    public ProductClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("com.tartanga.grupo4.product.product");
    }
    
    @Override
    public List<Product> findAllProducts(GenericType<List<Product>> responseType) throws WebApplicationException{
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    
    @Override
    public void linkCustomer(Product requestEntity, String id) throws WebApplicationException{
       webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }
    
    @Override
    public List<Product> find(GenericType<List<Product>> responseType, String from, String to) throws WebApplicationException{
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }

    

    
}
