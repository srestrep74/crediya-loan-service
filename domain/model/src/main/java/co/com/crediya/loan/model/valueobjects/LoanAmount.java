package co.com.crediya.loan.model.valueobjects;


import co.com.crediya.loan.model.exception.InvalidLoanRequestDataException;

import java.math.BigDecimal;

public class LoanAmount {

    private final BigDecimal value;

    private LoanAmount(BigDecimal value) {
        this.value = value;
    }

    public static LoanAmount of(BigDecimal value) {
        if (value == null) {
            throw new InvalidLoanRequestDataException("Loan amount cannot be null");
        }
        if( value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidLoanRequestDataException("Loan amount must be greater than zero");
        }
        return new LoanAmount(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoanAmount other = (LoanAmount) obj;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "LoanAmount{" + value + "}";
    }

}
