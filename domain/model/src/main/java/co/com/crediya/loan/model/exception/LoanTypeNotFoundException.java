package co.com.crediya.loan.model.exception;

public class LoanTypeNotFoundException extends RuntimeException {
    public LoanTypeNotFoundException(String message) {
        super(message);
    }
}
