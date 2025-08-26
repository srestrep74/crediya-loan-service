package co.com.crediya.loan.usecase.loanrequest;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanRequestStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanRequestUseCase {

    private final LoanRequestReactivePersistenceGateway loanRequestReactivePersistenceGateway;
    private final LoanTypeReactivePersistenceGateway loanTypeReactivePersistenceGateway;
    private final TransactionGateway transactionGateway;
    private final UserServiceGateway userServiceGateway;

    public Mono<LoanRequest> saveLoanRequest(LoanRequest loanRequest) {
        return transactionGateway.execute(
                userServiceGateway.findById(loanRequest.getCustomerId())
                        .switchIfEmpty(Mono.error(new UserNotFoundException("")))
                        .then(loanTypeReactivePersistenceGateway.findById(loanRequest.getLoanType().getId())
                                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException(""))))
                        .then(Mono.defer(() -> {
                            LoanRequest requestToSave = loanRequest.toBuilder()
                                    .status(LoanRequestStatus.PENDING_REVIEW)
                                    .build();
                            return loanRequestReactivePersistenceGateway.save(requestToSave);
                        }))
        )
    }

}
