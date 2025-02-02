/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.Transfers;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Alin
 */
public interface Itransfer {

    public List<Transfers> findAll_XML(GenericType<List<Transfers>> responseType) throws WebApplicationException;

    public List<Transfers> findBySender(GenericType<List<Transfers>> responseType, String sender) throws WebApplicationException;

    public List<Transfers> findByReciever(GenericType<List<Transfers>> responseType, String reciever) throws WebApplicationException;

    public List<Transfers> findByDate(GenericType<List<Transfers>> responseType, String startDate, String endDate) throws WebApplicationException;

    public Transfers findByID(GenericType<Transfers> responseType, String transferId) throws WebApplicationException;

    public void remove(String id) throws WebApplicationException;

    public void create_XML(Object requestEntity) throws WebApplicationException;

    public void edit_XML(Object requestEntity, String id) throws ClientErrorException;
}
