/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Iñi
 */
@XmlRootElement
public class CreditCard extends Product implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long creditCardNumber;
    
    protected Date creationDate;
    
    private Date expirationDate;
    
    private String cvv;
    
    private String pin;
    
    private Account account;
   
    private List<Movement> movementList;
    
    public CreditCard(){
    }
    
    
    public Long getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(Long creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
    
    //@XmlJavaTypeAdapter(DateAdapter.class)
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @XmlTransient
    public List<Movement> getMovementList() {
        return movementList;
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
    }
    
    //@XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardNumber != null ? creditCardNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.creditCardNumber == null && other.creditCardNumber != null) || (this.creditCardNumber != null && !this.creditCardNumber.equals(other.creditCardNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartanga.grupo4.creditcards.CreditCard[ id=" + creditCardNumber + " ]";
    }
    
}
