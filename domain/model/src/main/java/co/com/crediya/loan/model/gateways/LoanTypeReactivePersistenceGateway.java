package co.com.crediya.loan.model.gateways;

import co.com.crediya.loan.model.LoanType;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface LoanTypeReactivePersistenceGateway {
    Mono<LoanType> save(LoanType loanType);
    Mono<Boolean> existsById(Long id);
}
