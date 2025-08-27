package co.com.crediya.loan.model;

import co.com.crediya.loan.model.exception.InvalidLoanRequestDataException;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LoanAmountTest {

    @Test
    void shouldCreateValidLoanAmount() {
        LoanAmount amount = LoanAmount.of(BigDecimal.valueOf(1000));
        assertEquals(BigDecimal.valueOf(1000), amount.getValue());
    }

    @Test
    void shouldThrowExceptionWhenNull() {
        InvalidLoanRequestDataException exception = assertThrows(
                InvalidLoanRequestDataException.class,
                () -> LoanAmount.of(null)
        );
        assertEquals("Loan amount cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenZero() {
        InvalidLoanRequestDataException exception = assertThrows(
                InvalidLoanRequestDataException.class,
                () -> LoanAmount.of(BigDecimal.ZERO)
        );
        assertEquals("Loan amount must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNegative() {
        InvalidLoanRequestDataException exception = assertThrows(
                InvalidLoanRequestDataException.class,
                () -> LoanAmount.of(BigDecimal.valueOf(-500))
        );
        assertEquals("Loan amount must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        LoanAmount amount1 = LoanAmount.of(BigDecimal.valueOf(1500));
        LoanAmount amount2 = LoanAmount.of(BigDecimal.valueOf(1500));

        assertEquals(amount1, amount2);
        assertEquals(amount1.hashCode(), amount2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        LoanAmount amount1 = LoanAmount.of(BigDecimal.valueOf(2000));
        LoanAmount amount2 = LoanAmount.of(BigDecimal.valueOf(3000));

        assertNotEquals(amount1, amount2);
    }

    @Test
    void shouldNotBeEqualToOtherObjects() {
        LoanAmount amount = LoanAmount.of(BigDecimal.TEN);
        assertNotEquals(amount, null);
        assertNotEquals(amount, "not-a-loanamount");
    }

    @Test
    void shouldReturnProperToString() {
        LoanAmount amount = LoanAmount.of(BigDecimal.valueOf(2500));
        assertEquals("LoanAmount{2500}", amount.toString());
    }
}
