
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.models;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 2dami
 */
            

@XmlRootElement
public class Account extends Product implements Serializable {
    private static final long serialVersionUID = 1L;
    

    private String accountNumber;
    
    private Double balance;
    
    protected Date creationDate;

    private List<CreditCard> creditCardList;
    
    
    private List<Transfers> outgoingTransfers;
    
 
    private List<Transfers> incomingTransfers;

    public Account() {
        this.creationDate = super.creationDate;
    }
    public Account(String accountNumber, Double balance, Date creationDate){
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlTransient
    public List<Transfers> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    public void setOutgoingTransfers(List<Transfers> outgoingTransfers) {
        this.outgoingTransfers = outgoingTransfers;
    }

    @XmlTransient
    public List<Transfers> getIncomingTransfers() {
        return incomingTransfers;
    }

    public void setIncomingTransfers(List<Transfers> incomingTransfers) {
        this.incomingTransfers = incomingTransfers;
    }
    
    //relaciones
    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="customer_account",schema="rovobankDB", joinColumns = @JoinColumn(name="IDProduct", referencedColumnName = "IDProduct"),
                inverseJoinColumns = @JoinColumn(name="logIn",referencedColumnName = "logIn"))
    private Set<Customer> customers;

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }*/

    @XmlTransient
    public List<CreditCard> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<CreditCard> creditCardList) {
        this.creditCardList = creditCardList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (IDProduct != null ? IDProduct.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.IDProduct == null && other.IDProduct != null) || (this.IDProduct != null && !this.IDProduct.equals(other.IDProduct))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartanga.grupo4.accounts.Accounts[ id=" + IDProduct + " ]";
    }
    
}
