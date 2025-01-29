/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;


import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Iñi
 */

@XmlRootElement
public class Movement implements Serializable {

    private static final long serialVersionUID = 1L;
    
      private Long IDMovement;
    
    private Date transactionDate;
    
    private Double amount;
    
    private Currency currency;
    
      private CreditCard creditCard;
    
    public Movement(){}

    public Long getIDMovement() {
        return IDMovement;
    }

    public void setIDMovement(Long IDMovement) {
        this.IDMovement = IDMovement;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (IDMovement != null ? IDMovement.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movement)) {
            return false;
        }
        Movement other = (Movement) object;
        if ((this.IDMovement == null && other.IDMovement != null) || (this.IDMovement != null && !this.IDMovement.equals(other.IDMovement))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartanga.grupo4.creditcards.Movements[ id=" + IDMovement + " ]";
    }
    
}
