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
 * @author Alin
 */
@XmlRootElement
public class Transfers implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer transferId;
    
    private String sender;
    
    private String reciever;

    private Date transferDate;
    
    private Double Amount;

    private Currency currency;
    

    private Account sourceAccount;


    private Account destinationAccount;
    
    public Transfers(){
        this.transferId=0;
        this.sender="";
        this.reciever="";
        this.transferDate=null;
        this.Amount=0.0;
        this.currency=Currency.EURO;
    }
    public Transfers(Integer transferId,String sender,String reciever,Date transferDate,Double Amount,Currency currency){
        this.transferId=transferId;
        this.sender=sender;
        this.reciever=reciever;
        this.transferDate=transferDate;
        this.Amount=Amount;
        this.currency = currency;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double Amount) {
        this.Amount = Amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transferId != null ? transferId.hashCode() : 0);
        return hash;
    }
    
     @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transfers)) {
            return false;
        }
        Transfers other = (Transfers) object;
        if ((this.transferId == null && other.transferId != null) || (this.transferId != null && !this.transferId.equals(other.transferId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transfers{" + "transferId=" + transferId + ", sender=" + sender + ", reciever=" + reciever + ", transferDate=" + transferDate + ", Amount=" + Amount + ", currency=" + currency + '}';
    }
    
}
