package co.com.crediya.loan.model;

import co.com.crediya.loan.model.exception.InvalidLoanRequestDataException;
import co.com.crediya.loan.model.valueobjects.LoanTerm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanTermTest {

    @Test
    void shouldCreateValidLoanTerm() {
        LoanTerm term = LoanTerm.of(12);
        assertEquals(12, term.getValue());
    }

    @Test
    void shouldThrowExceptionWhenNull() {
        InvalidLoanRequestDataException exception = assertThrows(
                InvalidLoanRequestDataException.class,
                () -> LoanTerm.of(null)
        );
        assertEquals("Loan term cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenZero() {
        InvalidLoanRequestDataException exception = assertThrows(
                InvalidLoanRequestDataException.class,
                () -> LoanTerm.of(0)
        );
        assertEquals("Loan term must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNegative() {
        InvalidLoanRequestDataException exception = assertThrows(
                InvalidLoanRequestDataException.class,
                () -> LoanTerm.of(-6)
        );
        assertEquals("Loan term must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        LoanTerm term1 = LoanTerm.of(24);
        LoanTerm term2 = LoanTerm.of(24);

        assertEquals(term1, term2);
        assertEquals(term1.hashCode(), term2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        LoanTerm term1 = LoanTerm.of(36);
        LoanTerm term2 = LoanTerm.of(48);

        assertNotEquals(term1, term2);
    }

    @Test
    void shouldNotBeEqualToOtherObjects() {
        LoanTerm term = LoanTerm.of(18);
        assertNotEquals(term, null);
        assertNotEquals(term, "not-a-loanterm");
    }

    @Test
    void shouldReturnProperToString() {
        LoanTerm term = LoanTerm.of(60);
        assertEquals("LoanTerm{60 months}", term.toString());
    }
}
