package co.com.crediya.loan.api;

import co.com.crediya.loan.api.config.ExceptionConfig;
import co.com.crediya.loan.api.dto.v1.CreateLoanRequest;
import co.com.crediya.loan.api.dto.v1.CreateLoanResponse;
import co.com.crediya.loan.api.handler.v1.LoanRequestHandler;
import co.com.crediya.loan.api.router.LoanRequestRouter;
import co.com.crediya.loan.api.router.RouterRest;
import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanRequestStatus;
import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.exception.LoanTypeNotFoundException;
import co.com.crediya.loan.model.exception.UserNotFoundException;
import co.com.crediya.loan.model.valueobjects.Email;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import co.com.crediya.loan.model.valueobjects.LoanTerm;
import co.com.crediya.loan.usecase.loanrequest.LoanRequestUseCase;
import co.com.crediya.loan.usecase.loantype.LoanTypeUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(
        classes = {
                RouterRest.class,
                LoanRequestRouter.class,
                LoanRequestHandler.class,
                ExceptionConfig.class
        }
)
@WebFluxTest
class LoanRequestHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    LoanRequestUseCase loanRequestUseCase;

    @MockitoBean
    LoanTypeUseCase loanTypeUseCase;

    private CreateLoanRequest validRequest;
    private CreateLoanResponse expectedResponse;
    private CreateLoanRequest invalidEmailRequest;
    private CreateLoanRequest invalidAmountRequest;
    private LoanType mockLoanType;
    private LoanRequest mockLoanRequest;

    private final String loansEndpoint = "/api/v1/loans-requests";

    @BeforeEach
    void setUp() {
        validRequest = new CreateLoanRequest(
                new BigDecimal("15000.00"),
                24,
                "john.doe@example.com",
                1L,
                1001L
        );

        mockLoanType = LoanType.builder()
                .id(1L)
                .name("Personal Loan")
                .autoValidation(true)
                .build();

        mockLoanRequest = LoanRequest.builder()
                .id(101L)
                .amount(LoanAmount.of(new BigDecimal("15000.00")))
                .loanTerm(LoanTerm.of(24))
                .email(Email.of("john.doe@example.com"))
                .loanType(mockLoanType)
                .customerId(1001L)
                .status(LoanRequestStatus.PENDING_REVIEW)
                .build();

        expectedResponse = new CreateLoanResponse(
                101L,
                validRequest.amount(),
                validRequest.loanTerm(),
                validRequest.email(),
                validRequest.loanTypeId(),
                validRequest.customerId(),
                LoanRequestStatus.PENDING_REVIEW.name()
        );

        invalidEmailRequest = new CreateLoanRequest(
                new BigDecimal("15000.00"),
                24,
                "bad-email-format",
                1L,
                1001L
        );

        invalidAmountRequest = new CreateLoanRequest(
                new BigDecimal("-1.00"),
                24,
                "john.doe@example.com",
                1L,
                1001L
        );
    }

    @Test
    void shouldCreateLoanRequestSuccessfully() {
        when(loanTypeUseCase.findById(validRequest.loanTypeId()))
                .thenReturn(Mono.just(mockLoanType));
        when(loanRequestUseCase.saveLoanRequest(any()))
                .thenReturn(Mono.just(mockLoanRequest));

        webTestClient.post()
                .uri(loansEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateLoanResponse.class)
                .value(response -> {
                    Assertions.assertThat(response.id()).isEqualTo(expectedResponse.id());
                    Assertions.assertThat(response.amount()).isEqualTo(expectedResponse.amount());
                    Assertions.assertThat(response.loanTerm()).isEqualTo(expectedResponse.loanTerm());
                    Assertions.assertThat(response.email()).isEqualTo(expectedResponse.email());
                    Assertions.assertThat(response.loanTypeId()).isEqualTo(expectedResponse.loanTypeId());
                    Assertions.assertThat(response.customerId()).isEqualTo(expectedResponse.customerId());
                    Assertions.assertThat(response.status()).isEqualTo(expectedResponse.status());
                });
    }

    @Test
    void shouldReturnNotFoundWhenLoanTypeDoesNotExist() {
        Long missingLoanTypeId = 99L;
        CreateLoanRequest req = new CreateLoanRequest(
                validRequest.amount(),
                validRequest.loanTerm(),
                validRequest.email(),
                missingLoanTypeId,
                validRequest.customerId()
        );

        when(loanTypeUseCase.findById(missingLoanTypeId))
                .thenReturn(Mono.error(new LoanTypeNotFoundException("LoanType with id " + missingLoanTypeId + " not found")));

        webTestClient.post()
                .uri(loansEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("LoanTypeNotFoundException")
                .jsonPath("$.message").isEqualTo("LoanType with id " + missingLoanTypeId + " not found");
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        when(loanTypeUseCase.findById(validRequest.loanTypeId()))
                .thenReturn(Mono.just(mockLoanType));
        when(loanRequestUseCase.saveLoanRequest(any()))
                .thenReturn(Mono.error(new UserNotFoundException("User with id " + validRequest.customerId() + " not found")));

        webTestClient.post()
                .uri(loansEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("UserNotFoundException")
                .jsonPath("$.message").isEqualTo("User with id " + validRequest.customerId() + " not found");
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsInvalid() {
        when(loanTypeUseCase.findById(invalidAmountRequest.loanTypeId()))
                .thenReturn(Mono.just(mockLoanType));

        webTestClient.post()
                .uri(loansEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidAmountRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("InvalidLoanRequestDataException")
                .jsonPath("$.message").isEqualTo("Loan amount must be greater than zero");
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() {
        when(loanTypeUseCase.findById(invalidEmailRequest.loanTypeId()))
                .thenReturn(Mono.just(mockLoanType));

        webTestClient.post()
                .uri(loansEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmailRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("InvalidLoanRequestDataException")
                .jsonPath("$.message").isEqualTo("email must have a valid format");
    }
}
