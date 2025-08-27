package co.com.crediya.loan.consumer;

import co.com.crediya.loan.consumer.response.ExistsUserResponse;
import co.com.crediya.loan.model.gateways.UserServiceGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceAdapter implements UserServiceGateway {
    private final WebClient client;

    @CircuitBreaker(name = "userServiceExistsById" , fallbackMethod = "existsByIdFallback")
    public Mono<Boolean> existsById(Long userId) {
        final String uri = "/api/v1/users/{userId}/exists";

        return client
                .get()
                .uri(uri, userId)
                .retrieve()
                .bodyToMono(ExistsUserResponse.class)
                .map(ExistsUserResponse::exists);
    }

    public Mono<Boolean> existsByIdFallback(Long userId, Exception ex) {
        return Mono.just(false);
    }
}
