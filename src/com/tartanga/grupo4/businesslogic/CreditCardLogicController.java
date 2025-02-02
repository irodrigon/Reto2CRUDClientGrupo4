/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.businesslogic;

import com.tartanga.grupo4.models.CreditCard;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public interface CreditCardLogicController {
    
  public List<CreditCard> findAllCreditCards(GenericType<List<CreditCard>> responseType);
  
  public void deleteCreditCardByCardNumber(String creditCardNumber);
  
  public void createCreditCard(CreditCard requestEntity);
  
  public void updateCreditCard(CreditCard requestEntity, String id);
  
  public List<CreditCard> findCreditCardByCardNumber(GenericType<List<CreditCard>> responseType, String creditCardNumber);
  
  public List<CreditCard> findCreditCardByCreationDate(GenericType<List<CreditCard>> responseType, String startDate, String endDate);
  
  public List<CreditCard> findCreditCardByExpirationDate(GenericType<List<CreditCard>> responseType, String startDate, String endDate);
  
}
