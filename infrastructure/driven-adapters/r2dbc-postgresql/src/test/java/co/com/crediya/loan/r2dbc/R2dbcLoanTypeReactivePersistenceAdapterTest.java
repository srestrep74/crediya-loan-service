package co.com.crediya.loan.r2dbc;

import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.valueobjects.InterestRate;
import co.com.crediya.loan.model.valueobjects.MoneyRange;
import co.com.crediya.loan.r2dbc.adapters.loantype.LoanTypeReactiveRepository;
import co.com.crediya.loan.r2dbc.adapters.loantype.R2dbcLoanTypeReactivePersistenceAdapter;
import co.com.crediya.loan.r2dbc.entity.LoanTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class R2dbcLoanTypeReactivePersistenceAdapterTest {

    @Mock
    private LoanTypeReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private R2dbcLoanTypeReactivePersistenceAdapter adapter;

    private LoanTypeEntity entity;
    private LoanType loanType;

    @BeforeEach
    void setUp() {
        entity = LoanTypeEntity.builder()
                .id(1L)
                .name("Personal Loan")
                .minAmount(new BigDecimal("1000"))
                .maxAmount(new BigDecimal("20000"))
                .interestRate(new BigDecimal("0.15"))
                .autoValidation(true)
                .build();

        loanType = LoanType.builder()
                .id(1L)
                .name("Personal Loan")
                .moneyRange(MoneyRange.of(new BigDecimal("1000"), new BigDecimal("20000")))
                .interestRate(InterestRate.of(new BigDecimal("0.15")))
                .autoValidation(true)
                .build();
    }

    @Test
    void shouldFindById() {
        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, LoanType.class)).thenReturn(loanType);

        StepVerifier.create(adapter.findById(1L))
                .expectNextMatches(result ->
                        result.getId().equals(loanType.getId()) &&
                                result.getName().equals(loanType.getName()) &&
                                result.getMoneyRange().getMin().compareTo(loanType.getMoneyRange().getMin()) == 0 &&
                                result.getMoneyRange().getMax().compareTo(loanType.getMoneyRange().getMax()) == 0 &&
                                result.getInterestRate().getValue().compareTo(loanType.getInterestRate().getValue()) == 0 &&
                                result.isAutoValidation() == loanType.isAutoValidation()
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(99L))
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenExistsById() {
        when(repository.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsById(1L))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenNotExistsById() {
        when(repository.existsById(1L)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsById(1L))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldPropagateErrorOnFindById() {
        when(repository.findById(1L)).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(adapter.findById(1L))
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("DB error"))
                .verify();
    }
}
