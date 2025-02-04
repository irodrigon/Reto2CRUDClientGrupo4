package com.tartanga.grupo4.models;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author egure
 */
@XmlRootElement
public class Loan extends Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long loanId;
    protected Date creationDate;
    private Integer interest;
    private Date startDate;
    private Date endDate;
    private Double amount;
    private Integer period;
    
    public Loan(){
        this.creationDate = super.creationDate;
    }

    // Getters y Setters

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Integer getInterest() {
        return interest;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }


    @Override
    public Date getCreationDate() {
        return creationDate;
    }


    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loanId != null ? loanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the loanId fields are not set
        if (!(object instanceof Loan)) {
            return false;
        }
        Loan other = (Loan) object;
        if ((this.loanId == null && other.loanId != null) || (this.loanId != null && !this.loanId.equals(other.loanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tartanga.grupo4.loans.Loan[ loanId=" + loanId + " ]";
    }
}
