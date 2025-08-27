package co.com.crediya.loan.api.mapper;

import co.com.crediya.loan.api.dto.v1.CreateLoanRequest;
import co.com.crediya.loan.api.dto.v1.CreateLoanResponse;
import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.valueobjects.Email;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import co.com.crediya.loan.model.valueobjects.LoanTerm;

public final class CreateLoanRequestMapper {

    private CreateLoanRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static LoanRequest toDomain(CreateLoanRequest createLoanRequest, LoanType loanType) {
        if (createLoanRequest == null) {
            throw new IllegalArgumentException("CreateLoanRequest cannot be null");
        }
        if (loanType == null) {
            throw new IllegalArgumentException("LoanType cannot be null");
        }

        return LoanRequest.builder()
                .amount(LoanAmount.of(createLoanRequest.amount()))
                .loanTerm(LoanTerm.of(createLoanRequest.loanTerm()))
                .email(Email.of(createLoanRequest.email()))
                .loanType(loanType)
                .customerId(createLoanRequest.customerId())
                .build();
    }

    public static CreateLoanResponse toDto(LoanRequest loanRequest) {
        if (loanRequest == null) {
            throw new IllegalArgumentException("LoanRequest cannot be null");
        }
        if (loanRequest.getLoanType() == null) {
            throw new IllegalArgumentException("LoanRequest.loanType cannot be null");
        }

        return new CreateLoanResponse(
                loanRequest.getId(),
                loanRequest.getAmount().getValue(),
                loanRequest.getLoanTerm().getValue(),
                loanRequest.getEmail().getValue(),
                loanRequest.getLoanType().getId(),
                loanRequest.getCustomerId(),
                loanRequest.getStatus().name()
        );
    }
}
