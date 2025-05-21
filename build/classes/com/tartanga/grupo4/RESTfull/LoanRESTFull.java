/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.RESTfull;

import com.tartanga.grupo4.businesslogic.ILoan;
import com.tartanga.grupo4.businesslogic.LoanFactory;
import com.tartanga.grupo4.models.Loan;
import java.time.LocalDate;
import java.util.List;
import static javafx.scene.input.KeyCode.T;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:LoanFacadeREST
 * [com.tartanga.grupo4.loans.loan]<br>
 * USAGE:
 * <pre>
 *        LoanRESTFull client = new LoanRESTFull();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author egure
 */
public class LoanRESTFull implements ILoan {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Reto2CRUDServerGrupo4/webresources";

    public LoanRESTFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("com.tartanga.grupo4.loans.loan");
    }

    @Override
    public List<Loan> findByRemainingBalance(GenericType<List<Loan>> responseType, String balance) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("balance/{0}", new Object[]{balance}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    @Override
    public void edit_XML(Object requestEntity, Integer id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Loan.class);
    }

    /**
     * public void edit_JSON(Object requestEntity, String id) throws
     * WebApplicationException {
     * webTarget.path(java.text.MessageFormat.format("{0}", new
     * Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity,
     * javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }*
     */
    /**
     * public List Loan throws WebApplicationException { WebTarget resource =
     * webTarget; resource = resource.path(java.text.MessageFormat.format("{0}",
     * new Object[]{id})); 
    }*
     */
    @Override
    public List<Loan> findRange_XML(GenericType<List<Loan>> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * public List Loan,
     * String from, String to) throws WebApplicationException { WebTarget
     * resource = webTarget; resource =
     * resource.path(java.text.MessageFormat.format("{0}/{1}", new
     * Object[]{from, to})); return
     * resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }*
     */
    @Override
    public List<Loan> findByDates(GenericType<List<Loan>> responseType, String startDate, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Loan.class);
    }

    /**
     * public void create_JSON(Object requestEntity) throws
     * WebApplicationException {
     * webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity,
     * javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }*
     */
    @Override
    public List<Loan> findByInterestRate(GenericType<List<Loan>> responseType, String interestRate) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("interestRate/{0}", new Object[]{interestRate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public List<Loan> findAll_XML(GenericType<List<Loan>> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * public List Loan 
     * throws WebApplicationException { WebTarget resource = webTarget; return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);*
     */
    @Override
    public List<Loan> findByUser(GenericType<List<Loan>> responseType, String logIn) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("logIn/{0}", new Object[]{logIn}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void remove(Integer id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(Loan.class);
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public List<Loan> getAllLoans(GenericType<List<Loan>> genericType) {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(genericType);
    }

    @Override
    public List<Loan> find_XML(GenericType<List<Loan>> responseType, Integer id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public List<Loan> getLoansByDate(GenericType<List<Loan>> genericType, LocalDate value, LocalDate value0) {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byDates/{0}/{1}", new Object[]{value, value0}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(genericType);
    }

}
