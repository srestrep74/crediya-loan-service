package co.com.crediya.loan.model.exception;

public class InvalidLoanRequestDataException extends RuntimeException {
    public InvalidLoanRequestDataException(String message) {
        super(message);
    }
}
