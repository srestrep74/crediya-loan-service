package co.com.crediya.loan.model.exception;

public class InvalidLoanTypeDataException extends RuntimeException {
    public InvalidLoanTypeDataException(String message) {
        super(message);
    }
}
