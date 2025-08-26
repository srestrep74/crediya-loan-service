package co.com.crediya.loan.model.valueobjects;

import co.com.crediya.loan.model.exception.InvalidLoanRequestDataException;

public class LoanTerm {

    private final Integer months;

    private LoanTerm(Integer months) {
        this.months = months;
    }

    public static LoanTerm of(Integer months) {
        if (months == null) {
            throw new InvalidLoanRequestDataException("Loan term cannot be null");
        }

        if (months <= 0) {
            throw new InvalidLoanRequestDataException("Loan term must be greater than zero");
        }

        return new LoanTerm(months);
    }

    public Integer getValue() {
        return months;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoanTerm other = (LoanTerm) obj;
        return months.equals(other.months);
    }

    @Override
    public int hashCode() {
        return months.hashCode();
    }
}
