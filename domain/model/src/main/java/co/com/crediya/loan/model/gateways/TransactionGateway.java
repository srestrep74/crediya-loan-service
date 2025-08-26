package co.com.crediya.loan.model.gateways;

import reactor.core.publisher.Mono;

public interface TransactionGateway {
    <T> Mono<T> execute(Mono<T> action);
}
