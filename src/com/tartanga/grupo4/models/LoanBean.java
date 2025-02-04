package com.tartanga.grupo4.models;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import static org.eclipse.persistence.expressions.ExpressionOperator.currentDate;

/**
 *
 * @author egure
 */
@XmlRootElement
public class LoanBean extends Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long loanId;
    protected Date creationDate;
    private Integer interest;
    private Date startDate;
    private Date endDate;
    private Double amount;
    private Integer period;
    private Double rAmount;

    public LoanBean() {
        super();
    }

    public LoanBean(Long loanId, Date creationDate, Integer interest, Date endDate, Double amount, Double rAmount) {
        super(loanId, creationDate, interest, endDate, amount, rAmount);
    }
}

    
   