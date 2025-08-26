package co.com.crediya.loan.model.valueobjects;

import co.com.crediya.loan.model.exception.InvalidLoanTypeDataException;

import java.math.BigDecimal;

public class MoneyRange {

    private final BigDecimal min;
    private final BigDecimal max;

    private MoneyRange(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    public static MoneyRange of(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new InvalidLoanTypeDataException("Money range cannot be null");
        }
        if (min.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidLoanTypeDataException("Minimum must be >= 0");
        }
        if (max.compareTo(min) < 0) {
            throw new InvalidLoanTypeDataException("Maximum must be >= minimum");
        }
        return new MoneyRange(min, max);
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MoneyRange other = (MoneyRange) obj;
        return min.equals(other.min) && max.equals(other.max);
    }

    @Override
    public int hashCode() {
        return min.hashCode() + max.hashCode();
    }
}
