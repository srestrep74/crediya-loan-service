package co.com.crediya.loan.r2dbc;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanRequestStatus;
import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.valueobjects.Email;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import co.com.crediya.loan.model.valueobjects.LoanTerm;
import co.com.crediya.loan.r2dbc.adapters.loanrequest.LoanRequestReactiveRepository;
import co.com.crediya.loan.r2dbc.adapters.loanrequest.R2dbcLoanRequestReactivePersistenceAdapter;
import co.com.crediya.loan.r2dbc.entity.LoanRequestEntity;
import co.com.crediya.loan.r2dbc.mapper.LoanRequestMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class R2dbcLoanRequestReactivePersistenceAdapterTest {

    @Mock
    private LoanRequestReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private LoanRequestMapper loanRequestMapper;

    @InjectMocks
    private R2dbcLoanRequestReactivePersistenceAdapter adapter;

    private LoanRequestEntity loanRequestEntity;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        loanRequestEntity = LoanRequestEntity.builder()
                .id(1L)
                .customerId(10L)
                .loanTypeId(100L)
                .amount(new BigDecimal("15000.00"))
                .loanTerm(24)
                .email("john.doe@test.com")
                .statusId(1L)
                .build();

        loanRequest = LoanRequest.builder()
                .id(1L)
                .customerId(10L)
                .loanType(LoanType.builder().id(100L).name("Personal Loan").build())
                .amount(LoanAmount.of(new BigDecimal("15000.00")))
                .loanTerm(LoanTerm.of(24))
                .email(Email.of("john.doe@test.com"))
                .status(LoanRequestStatus.PENDING_REVIEW)
                .build();
    }

    @Test
    void shouldSaveLoanRequest() {
        when(loanRequestMapper.toData(loanRequest)).thenReturn(loanRequestEntity);
        when(repository.save(any())).thenReturn(Mono.just(loanRequestEntity));
        when(loanRequestMapper.toEntity(loanRequestEntity)).thenReturn(loanRequest);

        StepVerifier.create(adapter.save(loanRequest))
                .expectNextMatches(saved ->
                        saved.getId().equals(loanRequest.getId()) &&
                                saved.getAmount().getValue().compareTo(loanRequest.getAmount().getValue()) == 0 &&
                                saved.getLoanTerm().getValue().equals(loanRequest.getLoanTerm().getValue()) &&
                                saved.getEmail().getValue().equals(loanRequest.getEmail().getValue()) &&
                                saved.getStatus() == LoanRequestStatus.PENDING_REVIEW &&
                                saved.getLoanType().getId().equals(loanRequest.getLoanType().getId())
                )
                .verifyComplete();
    }

    @Test
    void shouldConvertToData() {
        when(loanRequestMapper.toData(loanRequest)).thenReturn(loanRequestEntity);

        LoanRequestEntity result = adapter.toData(loanRequest);

        assert result.getId().equals(loanRequestEntity.getId());
        assert result.getCustomerId().equals(loanRequestEntity.getCustomerId());
        assert result.getAmount().compareTo(loanRequestEntity.getAmount()) == 0;
        assert result.getLoanTerm().equals(loanRequestEntity.getLoanTerm());
        assert result.getEmail().equals(loanRequestEntity.getEmail());
    }

    @Test
    void shouldConvertToEntity() {
        when(loanRequestMapper.toEntity(loanRequestEntity)).thenReturn(loanRequest);

        LoanRequest result = adapter.toEntity(loanRequestEntity);

        assert result.getId().equals(loanRequest.getId());
        assert result.getCustomerId().equals(loanRequest.getCustomerId());
        assert result.getAmount().getValue().compareTo(loanRequest.getAmount().getValue()) == 0;
        assert result.getLoanTerm().getValue().equals(loanRequest.getLoanTerm().getValue());
        assert result.getEmail().getValue().equals(loanRequest.getEmail().getValue());
        assert result.getStatus() == LoanRequestStatus.PENDING_REVIEW;
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {
        when(loanRequestMapper.toData(loanRequest)).thenReturn(loanRequestEntity);
        when(repository.save(any())).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(adapter.save(loanRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("DB error")
                )
                .verify();
    }
}
