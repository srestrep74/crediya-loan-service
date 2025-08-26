package co.com.crediya.loan.model.valueobjects;

import co.com.crediya.loan.model.exception.InvalidLoanTypeDataException;

import java.math.BigDecimal;

public class InterestRate {

    private final BigDecimal percentage;

    private InterestRate(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public static InterestRate of(BigDecimal percentage) {
        if (percentage == null) {
            throw new InvalidLoanTypeDataException("Interest rate cannot be null");
        }
        if (percentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidLoanTypeDataException("Interest rate must be >= 0");
        }
        return new InterestRate(percentage);
    }

    public BigDecimal getValue() {
        return percentage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InterestRate other = (InterestRate) obj;
        return percentage.equals(other.percentage);
    }

    @Override
    public int hashCode() {
        return percentage.hashCode();
    }
}
