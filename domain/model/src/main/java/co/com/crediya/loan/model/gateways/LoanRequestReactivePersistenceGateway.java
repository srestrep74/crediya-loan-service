package co.com.crediya.loan.model.gateways;

import co.com.crediya.loan.model.LoanRequest;
import reactor.core.publisher.Mono;

public interface LoanRequestReactivePersistenceGateway {
    Mono<LoanRequest> save(LoanRequest loanRequest);
}
