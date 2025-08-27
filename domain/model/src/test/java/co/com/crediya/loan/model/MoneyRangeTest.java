package co.com.crediya.loan.model;

import co.com.crediya.loan.model.exception.InvalidLoanTypeDataException;
import co.com.crediya.loan.model.valueobjects.MoneyRange;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyRangeTest {

    @Test
    void shouldCreateValidMoneyRange() {
        MoneyRange range = MoneyRange.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(5000));
        assertEquals(BigDecimal.valueOf(1000), range.getMin());
        assertEquals(BigDecimal.valueOf(5000), range.getMax());
    }

    @Test
    void shouldThrowExceptionWhenMinIsNull() {
        InvalidLoanTypeDataException exception = assertThrows(
                InvalidLoanTypeDataException.class,
                () -> MoneyRange.of(null, BigDecimal.valueOf(5000))
        );
        assertEquals("Money range cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMaxIsNull() {
        InvalidLoanTypeDataException exception = assertThrows(
                InvalidLoanTypeDataException.class,
                () -> MoneyRange.of(BigDecimal.valueOf(1000), null)
        );
        assertEquals("Money range cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMinIsNegative() {
        InvalidLoanTypeDataException exception = assertThrows(
                InvalidLoanTypeDataException.class,
                () -> MoneyRange.of(BigDecimal.valueOf(-1), BigDecimal.valueOf(1000))
        );
        assertEquals("Minimum must be >= 0", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMaxIsLessThanMin() {
        InvalidLoanTypeDataException exception = assertThrows(
                InvalidLoanTypeDataException.class,
                () -> MoneyRange.of(BigDecimal.valueOf(5000), BigDecimal.valueOf(1000))
        );
        assertEquals("Maximum must be >= minimum", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameValues() {
        MoneyRange range1 = MoneyRange.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(5000));
        MoneyRange range2 = MoneyRange.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(5000));

        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        MoneyRange range1 = MoneyRange.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(5000));
        MoneyRange range2 = MoneyRange.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(6000));

        assertNotEquals(range1, range2);
    }

    @Test
    void shouldNotBeEqualToOtherObjects() {
        MoneyRange range = MoneyRange.of(BigDecimal.ZERO, BigDecimal.TEN);
        assertNotEquals(range, null);
        assertNotEquals(range, "not-a-moneyrange");
    }
}
