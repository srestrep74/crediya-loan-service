package co.com.crediya.loan.usecase.loanrequest;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanRequestStatus;
import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.exception.LoanTypeNotFoundException;
import co.com.crediya.loan.model.exception.UserNotFoundException;
import co.com.crediya.loan.model.gateways.LoanRequestReactivePersistenceGateway;
import co.com.crediya.loan.model.gateways.LoanTypeReactivePersistenceGateway;
import co.com.crediya.loan.model.gateways.TransactionGateway;
import co.com.crediya.loan.model.gateways.UserServiceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanRequestUseCaseTest {

    @Mock
    private LoanRequestReactivePersistenceGateway loanRequestReactivePersistenceGateway;

    @Mock
    private LoanTypeReactivePersistenceGateway loanTypeReactivePersistenceGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private UserServiceGateway userServiceGateway;

    @InjectMocks
    private LoanRequestUseCase loanRequestUseCase;

    private LoanRequest loanRequest;
    private LoanType loanType;

    @BeforeEach
    void setup() {
        loanType = LoanType.builder().id(10L).name("Personal Loan").build();

        loanRequest = LoanRequest.builder()
                .id(1L)
                .customerId(100L)
                .loanType(loanType)
                .status(LoanRequestStatus.PENDING_REVIEW)
                .build();
    }

    @Test
    void shouldSaveLoanRequestWhenUserAndLoanTypeExist() {
        when(userServiceGateway.existsById(loanRequest.getCustomerId())).thenReturn(Mono.just(true));
        when(loanTypeReactivePersistenceGateway.existsById(loanType.getId())).thenReturn(Mono.just(true));
        when(loanRequestReactivePersistenceGateway.save(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(transactionGateway.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StepVerifier.create(loanRequestUseCase.saveLoanRequest(loanRequest))
                .expectNextMatches(saved -> saved.getStatus() == LoanRequestStatus.PENDING_REVIEW &&
                        saved.getLoanType().equals(loanType))
                .verifyComplete();

        verify(userServiceGateway).existsById(loanRequest.getCustomerId());
        verify(loanTypeReactivePersistenceGateway).existsById(loanType.getId());
        verify(loanRequestReactivePersistenceGateway).save(any());
        verify(transactionGateway).execute(any());
    }

    @Test
    void shouldThrowErrorWhenLoanTypeIsNull() {
        LoanRequest invalidRequest = loanRequest.toBuilder().loanType(null).build();

        StepVerifier.create(loanRequestUseCase.saveLoanRequest(invalidRequest))
                .expectError(LoanTypeNotFoundException.class)
                .verify();

        verifyNoInteractions(userServiceGateway, loanTypeReactivePersistenceGateway, loanRequestReactivePersistenceGateway, transactionGateway);
    }

    @Test
    void shouldThrowErrorWhenUserDoesNotExist() {
        when(userServiceGateway.existsById(loanRequest.getCustomerId())).thenReturn(Mono.just(false));
        when(loanTypeReactivePersistenceGateway.existsById(loanRequest.getLoanType().getId()))
                .thenReturn(Mono.just(true));
        when(transactionGateway.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StepVerifier.create(loanRequestUseCase.saveLoanRequest(loanRequest))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(userServiceGateway).existsById(loanRequest.getCustomerId());
        verifyNoInteractions(loanRequestReactivePersistenceGateway);
    }

    @Test
    void shouldThrowErrorWhenLoanTypeDoesNotExist() {
        when(userServiceGateway.existsById(loanRequest.getCustomerId())).thenReturn(Mono.just(true));
        when(loanTypeReactivePersistenceGateway.existsById(loanType.getId())).thenReturn(Mono.just(false));
        when(transactionGateway.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        StepVerifier.create(loanRequestUseCase.saveLoanRequest(loanRequest))
                .expectError(LoanTypeNotFoundException.class)
                .verify();

        verify(userServiceGateway).existsById(loanRequest.getCustomerId());
        verify(loanTypeReactivePersistenceGateway).existsById(loanType.getId());
        verifyNoInteractions(loanRequestReactivePersistenceGateway);
    }

    @Test
    void shouldPropagateErrorWhenTransactionFails() {
        when(userServiceGateway.existsById(loanRequest.getCustomerId())).thenReturn(Mono.just(true));
        when(loanTypeReactivePersistenceGateway.existsById(loanType.getId())).thenReturn(Mono.just(true));
        when(transactionGateway.execute(any())).thenReturn(Mono.error(new RuntimeException("Tx error")));

        StepVerifier.create(loanRequestUseCase.saveLoanRequest(loanRequest))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Tx error"))
                .verify();

        verify(userServiceGateway).existsById(loanRequest.getCustomerId());
        verify(loanTypeReactivePersistenceGateway).existsById(loanType.getId());
        verify(transactionGateway).execute(any());
        verifyNoInteractions(loanRequestReactivePersistenceGateway);
    }
}
