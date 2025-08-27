package co.com.crediya.loan.usecase.loantype;

import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.exception.LoanTypeNotFoundException;
import co.com.crediya.loan.model.gateways.LoanTypeReactivePersistenceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanTypeUseCaseTest {

    @Mock
    private LoanTypeReactivePersistenceGateway loanTypeReactivePersistenceGateway;

    @InjectMocks
    private LoanTypeUseCase loanTypeUseCase;

    private LoanType loanType;

    @BeforeEach
    void setup() {
        loanType = LoanType.builder()
                .id(1L)
                .name("Personal Loan")
                .build();
    }

    @Test
    void shouldReturnLoanTypeWhenExists() {
        when(loanTypeReactivePersistenceGateway.findById(1L))
                .thenReturn(Mono.just(loanType));

        StepVerifier.create(loanTypeUseCase.findById(1L))
                .expectNextMatches(result ->
                        result.getId().equals(loanType.getId()) &&
                                result.getName().equals(loanType.getName()))
                .verifyComplete();

        verify(loanTypeReactivePersistenceGateway).findById(1L);
    }

    @Test
    void shouldThrowErrorWhenLoanTypeDoesNotExist() {
        when(loanTypeReactivePersistenceGateway.findById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(loanTypeUseCase.findById(1L))
                .expectErrorMatches(ex -> ex instanceof LoanTypeNotFoundException &&
                        ex.getMessage().contains("LoanType with id 1 not found"))
                .verify();

        verify(loanTypeReactivePersistenceGateway).findById(1L);
    }
}
