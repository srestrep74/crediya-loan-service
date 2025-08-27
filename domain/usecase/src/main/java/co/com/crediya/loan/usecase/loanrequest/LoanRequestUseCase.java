package co.com.crediya.loan.usecase.loanrequest;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanRequestStatus;
import co.com.crediya.loan.model.exception.LoanTypeNotFoundException;
import co.com.crediya.loan.model.exception.UserNotFoundException;
import co.com.crediya.loan.model.gateways.LoanRequestReactivePersistenceGateway;
import co.com.crediya.loan.model.gateways.LoanTypeReactivePersistenceGateway;
import co.com.crediya.loan.model.gateways.TransactionGateway;
import co.com.crediya.loan.model.gateways.UserServiceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanRequestUseCase {

    private final LoanRequestReactivePersistenceGateway loanRequestReactivePersistenceGateway;
    private final LoanTypeReactivePersistenceGateway loanTypeReactivePersistenceGateway;
    private final TransactionGateway transactionGateway;
    private final UserServiceGateway userServiceGateway;

    public Mono<LoanRequest> saveLoanRequest(LoanRequest loanRequest) {
        return Mono.defer(() -> {
            if (loanRequest.getLoanType() == null) {
                return Mono.error(new LoanTypeNotFoundException("LoanType is null"));
            }
            return transactionGateway.execute(
                    this.validateUserExists(loanRequest.getCustomerId())
                            .then(this.validateLoanTypeExists(loanRequest.getLoanType().getId()))
                            .then(Mono.defer(() -> {
                                LoanRequest requestToSave = loanRequest.toBuilder()
                                        .status(LoanRequestStatus.PENDING_REVIEW)
                                        .build();
                                return loanRequestReactivePersistenceGateway.save(requestToSave);
                            }))
                            .map(savedRequest -> savedRequest.toBuilder()
                                    .loanType(loanRequest.getLoanType())
                                    .build())
            );
        });
    }

    private Mono<Void> validateUserExists(Long userId) {
        return userServiceGateway.existsById(userId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new UserNotFoundException("User with id " + userId + " not found"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> validateLoanTypeExists(Long loanTypeId) {
        return loanTypeReactivePersistenceGateway.existsById(loanTypeId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new LoanTypeNotFoundException("LoanType with id " + loanTypeId + " not found"));
                    }
                    return Mono.empty();
                });
    }

}
