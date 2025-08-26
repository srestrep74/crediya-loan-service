package co.com.crediya.loan.model.gateways;

import reactor.core.publisher.Mono;

public interface UserServiceGateway {
    Mono<Boolean> existsById(Long id);
}
