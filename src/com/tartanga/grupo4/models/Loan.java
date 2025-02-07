package com.tartanga.grupo4.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a loan product with various attributes such as loan ID, creation date,
 * interest rate, start date, end date, amount, and period.
 * This class extends the Product class and implements Serializable.
 * It provides methods to calculate the remaining amount based on the loan's terms.
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

    /**
     * Constructs a Loan object with specified parameters.
     *
     * @param loanId      The unique identifier for the loan.
     * @param creationDate The date the loan was created.
     * @param interest     The interest rate of the loan.
     * @param endDate     The date the loan ends.
     * @param amount      The total amount of the loan.
     * @param rAmount     The remaining amount of the loan (not used in constructor).
     */
    public Loan(Long loanId, Date creationDate, Integer interest, Date endDate, Double amount, Double rAmount) {
        this.loanId = loanId;
        this.creationDate = creationDate;
        this.interest = interest;
        this.endDate = endDate;
        this.amount = amount;
        // Note: The field rAmount is calculated automatically, not necessary to pass to the constructor
    }

    /**
     * Default constructor for Loan, initializes with default values.
     */
    public Loan() {
        this.IDProduct = super.IDProduct;
        this.creationDate = super.creationDate;
        this.interest = 0;
        this.startDate = super.creationDate;
        this.endDate = super.creationDate;
        this.amount = 0.0;
        this.period = 30;
    }

    // Getters and Setters

    public Integer getIDProduct() {
        return IDProduct;
    }

    public void setIDProduct(Integer IDProduct) {
        this.IDProduct = IDProduct;
    }

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
        // Warning - this method won't work in the case the loanId fields are not set
        if (!(object instanceof Loan)) {
            return false;
        }
        Loan other = (Loan) object;

        if ((this.loanId == null && other.loanId != null) || (this.loanId != null && !this.loanId.equals(other.loanId))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the Loan object.
     *
     * @return A string containing loan details.
     */
    @Override
    public String toString() {
        return "Loan{" + "loanId=" + loanId + ", creationDate=" + creationDate + ", interest=" + interest + ", startDate=" + startDate + ", endDate=" + endDate + ", amount=" + amount + ", period=" + period + '}';
    }

    /**
     * Calculates the remaining amount of the loan based on the current date,
     * start date, end date, interest rate, and period.
     *
     * @return The remaining amount of the loan, rounded to two decimal places.
     */
    public Double getRAmount() {
        LocalDate currentDate = LocalDate.now();
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (currentDate.isBefore(start)) {
            return amount;
        } else if (currentDate.isAfter(end)) {
            return 0.0;
        } else {
            long totalPeriods = ChronoUnit.DAYS.between(start, end) / this.period;
            long elapsedPeriods = ChronoUnit.DAYS.between(start, currentDate) / this.period;

            // Calculate the interest factor
            double interestFactor = 1 + (interest.doubleValue() / 100.0) * (elapsedPeriods / (double) totalPeriods);

            // Calculate the result and round to two decimal places
            double result = amount * interestFactor;
            return Math.round(result * 100.0) / 100.0;  // Round to 2 decimal places
        }
    }

    public void setStartDate(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }
}