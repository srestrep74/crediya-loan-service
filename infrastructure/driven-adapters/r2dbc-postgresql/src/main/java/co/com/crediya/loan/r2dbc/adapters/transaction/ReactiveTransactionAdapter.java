package co.com.crediya.loan.r2dbc.adapters.transaction;

import co.com.crediya.loan.model.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveTransactionAdapter implements TransactionGateway {

    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> execute(Mono<T> action) {
        return transactionalOperator.execute(status -> action).singleOrEmpty();
    }

}
