package co.com.crediya.loan.r2dbc.mapper;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanRequestStatus;
import co.com.crediya.loan.model.valueobjects.Email;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import co.com.crediya.loan.model.valueobjects.LoanTerm;
import co.com.crediya.loan.r2dbc.entity.LoanRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestMapper {

    public LoanRequest toEntity(LoanRequestEntity entity) {
        if (entity == null) {
            return null;
        }

        return LoanRequest.builder()
                .id(entity.getId())
                .amount(LoanAmount.of(entity.getAmount()))
                .loanTerm(LoanTerm.of(entity.getLoanTerm()))
                .email(Email.of(entity.getEmail()))
                .customerId(entity.getCustomerId())
                .status(mapStatus(entity.getStatusId()))
                .build();
    }

    public LoanRequestEntity toData(LoanRequest loanRequest) {
        if (loanRequest == null) {
            return null;
        }

        validateLoanRequest(loanRequest);

        return LoanRequestEntity.builder()
                .id(loanRequest.getId())
                .amount(loanRequest.getAmount().getValue())
                .loanTerm(loanRequest.getLoanTerm().getValue())
                .email(loanRequest.getEmail().getValue())
                .loanTypeId(loanRequest.getLoanType().getId())
                .customerId(loanRequest.getCustomerId())
                .statusId(loanRequest.getStatus().getCode())
                .build();
    }

    private void validateLoanRequest(LoanRequest loanRequest) {
        if (loanRequest.getLoanType() == null) {
            throw new IllegalArgumentException("LoanRequest.loanType cannot be null");
        }
        if (loanRequest.getStatus() == null) {
            throw new IllegalArgumentException("LoanRequest.status cannot be null");
        }
    }

    private LoanRequestStatus mapStatus(Long statusId) {
        if (statusId == null) {
            return null;
        }
        
        for (LoanRequestStatus status : LoanRequestStatus.values()) {
            if (status.getCode().equals(statusId)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("Unknown status ID: " + statusId);
    }
}
