package co.com.crediya.loan.model;

public enum LoanRequestStatus {
    PENDING_REVIEW(1L),
    APPROVED(2L),
    REJECTED(3L);

    private final Long code;

    LoanRequestStatus(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return code;
    }
}
