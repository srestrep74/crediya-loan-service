package co.com.crediya.loan.api.handler.v1;

import co.com.crediya.loan.api.dto.v1.CreateLoanRequest;
import co.com.crediya.loan.api.mapper.CreateLoanRequestMapper;
import co.com.crediya.loan.usecase.loanrequest.LoanRequestUseCase;
import co.com.crediya.loan.usecase.loantype.LoanTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanRequestHandler {

    private final LoanRequestUseCase loanRequestUseCase;
    private final LoanTypeUseCase loantypeUseCase;

    public Mono<ServerResponse> listenSaveLoan(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(CreateLoanRequest.class)
                .flatMap(dto ->
                        loantypeUseCase.findById(dto.loanTypeId())
                                .map(loanType -> CreateLoanRequestMapper.toDomain(dto, loanType))
                )
                .flatMap(loanRequestUseCase::saveLoanRequest)
                .map(CreateLoanRequestMapper::toDto)
                .flatMap(loanRequestResponse -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(loanRequestResponse)
                );
    }
}
