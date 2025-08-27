package co.com.crediya.loan.api.router;

import co.com.crediya.loan.api.handler.v1.LoanRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class LoanRequestRouter {

    private final LoanRequestHandler loanRequestHandler;

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .POST("/loans-requests", loanRequestHandler::listenSaveLoan)
                .build();
    }
}
