package co.com.crediya.loan.model;

import co.com.crediya.loan.model.exception.InvalidLoanTypeDataException;
import co.com.crediya.loan.model.valueobjects.InterestRate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class InterestRateTest {

    @Test
    void shouldCreateValidInterestRate() {
        InterestRate rate = InterestRate.of(BigDecimal.valueOf(5.5));
        assertEquals(BigDecimal.valueOf(5.5), rate.getValue());
    }
    @Test
    void shouldAllowZeroInterestRate() {
        InterestRate rate = InterestRate.of(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, rate.getValue());
    }

    @Test
    void shouldThrowExceptionWhenNull() {
        InvalidLoanTypeDataException exception = assertThrows(
                InvalidLoanTypeDataException.class,
                () -> InterestRate.of(null)
        );
        assertEquals("Interest rate cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNegative() {
        InvalidLoanTypeDataException exception = assertThrows(
                InvalidLoanTypeDataException.class,
                () -> InterestRate.of(BigDecimal.valueOf(-1))
        );
        assertEquals("Interest rate must be >= 0", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        InterestRate rate1 = InterestRate.of(BigDecimal.valueOf(10));
        InterestRate rate2 = InterestRate.of(BigDecimal.valueOf(10));

        assertEquals(rate1, rate2);
        assertEquals(rate1.hashCode(), rate2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        InterestRate rate1 = InterestRate.of(BigDecimal.valueOf(10));
        InterestRate rate2 = InterestRate.of(BigDecimal.valueOf(15));

        assertNotEquals(rate1, rate2);
    }

    @Test
    void shouldNotBeEqualToOtherObjects() {
        InterestRate rate = InterestRate.of(BigDecimal.TEN);
        assertNotEquals(rate, "some-string");
        assertNotEquals(rate, null);
    }
}
