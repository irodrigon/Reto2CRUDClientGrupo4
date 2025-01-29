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

    public Loan(Long loanId, Date creationDate, Integer interest, Date endDate, Double amount, Double rAmount) {
    this.loanId = loanId;
    this.creationDate = creationDate;
    this.interest = interest;
    this.endDate = endDate;
    this.amount = amount;
    // Nota: El campo rAmount ya se calcula automáticamente, no es necesario pasarlo al constructor
    // this.rAmount = rAmount; // Si decides usar este campo, necesitas agregarlo en la clase Loan.
}

    public Loan() {
        this.IDProduct = super.IDProduct;
        this.creationDate = super.creationDate;
        this.interest = 0;
//        this.loanId = (long)IDProduct;
        this.startDate = super.creationDate;
        this.endDate = super.creationDate;
        this.amount = 00.0;
        this.period = 30;
    }

    public Integer getIDProduct() {
        return IDProduct;
    }

    public void setIDProduct(Integer IDProduct) {
        this.IDProduct = IDProduct;
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
        if (this.loanId == null && other.loanId != null) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "com.tartanga.grupo4.loans.Loan[ loanId=" + loanId + " ]";
//    }
    @Override
    public String toString() {
        return "Loan{" + "loanId=" + loanId + ", creationDate=" + creationDate + ", interest=" + interest + ", startDate=" + startDate + ", endDate=" + endDate + ", amount=" + amount + ", period=" + period + '}';
    }

//   public Double getRAmount() {
//    LocalDate currentDate = LocalDate.now();
//    LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//    if (currentDate.isBefore(start)) {
//        return amount;
//    } else if (currentDate.isAfter(end)) {
//        return 0.0;
//    } else {
//        long totalPeriods = ChronoUnit.DAYS.between(start, end) / this.period;
//        long elapsedPeriods = ChronoUnit.DAYS.between(start, currentDate) / this.period;
//
//        BigDecimal interestFactor = BigDecimal.valueOf(1 + (interest / 100.0) * (elapsedPeriods / (double) totalPeriods));
//        BigDecimal result = BigDecimal.valueOf(this.amount).multiply(interestFactor);
//        return result.setScale(2, RoundingMode.HALF_UP).doubleValue();
//    }
//}
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

            // Calcular el factor de interés
            double interestFactor = 1 + (interest.doubleValue() / 100.0) * (elapsedPeriods / (double) totalPeriods);

            // Asegurarse de que 'this.amount' sea un double
            double amountD = this.amount;

            // Calcular el resultado y redondear a dos decimales
            double result = amountD * interestFactor;
            return Math.round(result * 100.0) / 100.0;  // Redondear a 2 decimales
        }

    }
}
