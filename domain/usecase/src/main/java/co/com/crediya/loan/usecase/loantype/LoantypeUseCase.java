package co.com.crediya.loan.usecase.loantype;

import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.exception.LoanTypeNotFoundException;
import co.com.crediya.loan.model.gateways.LoanTypeReactivePersistenceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoantypeUseCase {

    private final LoanTypeReactivePersistenceGateway loanTypeReactivePersistenceGateway;

    public Mono<LoanType> findById(Long id) {
        return loanTypeReactivePersistenceGateway.findById(id)
                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException("LoanType with id " + id + " not found")));
    }

}
