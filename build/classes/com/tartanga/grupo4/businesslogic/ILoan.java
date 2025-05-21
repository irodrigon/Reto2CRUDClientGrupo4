package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Loan;
import java.time.LocalDate;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Interface for managing loans in the business logic layer.
 *
 * @author Aratz
 */
public interface ILoan {

    public List<Loan> findByRemainingBalance(GenericType<List<Loan>> responseType, String balance) throws WebApplicationException;
    //

    public String countREST() throws WebApplicationException;

    public void edit_XML(Object requestEntity, Integer id) throws WebApplicationException;

    public List<Loan> find_XML(GenericType<List<Loan>> responseType, Integer id) throws WebApplicationException;

    public List<Loan> findRange_XML(GenericType<List<Loan>> responseType, String from, String to) throws WebApplicationException;

    public List<Loan> findByDates(GenericType<List<Loan>> responseType, String startDate, String endDate) throws WebApplicationException;
    //

    public void create_XML(Object requestEntity) throws WebApplicationException;

    public List<Loan> findByInterestRate(GenericType<List<Loan>> responseType, String interestRate) throws WebApplicationException;
    //

    public List<Loan> findAll_XML(GenericType<List<Loan>> responseType) throws WebApplicationException;
    //

    public List<Loan> findByUser(GenericType<List<Loan>> responseType, String logIn) throws WebApplicationException;

    public void remove(Integer id) throws WebApplicationException;

    public void close();

    public List<Loan> getLoansByDate(GenericType<List<Loan>> genericType, LocalDate value, LocalDate value0);

    public List<Loan> getAllLoans(GenericType<List<Loan>> genericType);

}
