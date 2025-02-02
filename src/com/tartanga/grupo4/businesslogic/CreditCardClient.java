/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Admin;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import com.tartanga.grupo4.models.CreditCard;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

/**
 * Jersey REST client generated for REST resource:CreditCardFacadeREST
 * [com.tartanga.grupo4.creditcards.creditcard]<br>
 * USAGE:
 * <pre>
 * CreditCardClient client = new CreditCardClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author Iñi
 */
public class CreditCardClient implements CreditCardLogicController {

    private static Logger logger = Logger.getLogger(CreditCardClient.class.getName());

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Reto2CRUDServerGrupo4/webresources";

    public CreditCardClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("com.tartanga.grupo4.creditcards.creditcard");
    }

    public <T> T findCreditCardByexpirationDate_XML(Class<T> responseType, String startDate, String endDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("expirationDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findCreditCardByexpirationDate_JSON(Class<T> responseType, String startDate, String endDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("expirationDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String countREST() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void edit_XML(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void create_XML(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T findCreditCardByCreationDate_XML(Class<T> responseType, String startDate, String endDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("creationDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findCreditCardByCreationDate_JSON(Class<T> responseType, String startDate, String endDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("creationDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void remove(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    public <T> T findCreditCardByCreditCardNumber_XML(Class<T> responseType, String creditCardNumber) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("creditCardNumber/{0}", new Object[]{creditCardNumber}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findCreditCardByCreditCardNumber_JSON(Class<T> responseType, String creditCardNumber) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("creditCardNumber/{0}", new Object[]{creditCardNumber}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public List<CreditCard> findAllCreditCards(GenericType<List<CreditCard>> responseType) {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void deleteCreditCardByCardNumber(String creditCardNumber) {
        webTarget.path(java.text.MessageFormat.format("deleteByCardNumber/{0}", new Object[]{creditCardNumber})).request().delete();
    }

    @Override
    public void createCreditCard(CreditCard requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    @Override
    public void updateCreditCard(CreditCard requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    @Override
    public List<CreditCard> findCreditCardByCardNumber(GenericType<List<CreditCard>> responseType, String creditCardNumber) throws WebApplicationException {
        try {
            if (responseType == null) {
                logger.log(Level.INFO, "Credit card number not found:");
                throw new NotFoundException();
            } else {
                WebTarget resource = webTarget;
                resource = resource.path(java.text.MessageFormat.format("creditCardNumber/{0}", new Object[]{creditCardNumber}));
                return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
            }
        } catch (NotFoundException e) {
            logger.log(Level.INFO, "Credit card number not found: ", e.getMessage());
            throw new NotFoundException(e.getMessage());
        }
    }
    
     @Override
    public List<CreditCard> findCreditCardByCreationDate(GenericType<List<CreditCard>> responseType, String startDate, String endDate) throws WebApplicationException{
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("creationDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public List<CreditCard> findCreditCardByExpirationDate(GenericType<List<CreditCard>> responseType, String startDate, String endDate) throws WebApplicationException{
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("expirationDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }
}
