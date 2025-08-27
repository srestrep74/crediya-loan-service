package co.com.crediya.loan.api.router.documentation;

import co.com.crediya.loan.api.dto.v1.CreateLoanRequest;
import co.com.crediya.loan.api.dto.v1.CreateLoanResponse;
import co.com.crediya.loan.api.handler.v1.LoanRequestHandler;
import co.com.crediya.loan.api.helpers.ApiStandardError;
import co.com.crediya.loan.api.router.LoanRequestRouter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class LoanRequestRouterApiDocumentation {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/loans-requests",
                    method = RequestMethod.POST,
                    beanClass = LoanRequestHandler.class,
                    beanMethod = "listenSaveLoan",
                    operation = @Operation(
                            summary = "Create a new loan request",
                            description = "Creates a new loan request for a customer with a specific loan type",
                            operationId = "createLoanRequest",
                            tags = {"Loan Request Management"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CreateLoanRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Loan request created successfully",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CreateLoanResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad Request - Invalid input data",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApiStandardError.class),
                                                    examples = @ExampleObject(
                                                            name = "InvalidInput",
                                                            value = "{ \"timestamp\": \"2025-08-27T18:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"LoanTypeId is required\", \"path\": \"/api/v1/loans\" }"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Not Found - User or LoanType not found",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApiStandardError.class),
                                                    examples = @ExampleObject(
                                                            name = "UserOrLoanTypeNotFound",
                                                            value = "{ \"timestamp\": \"2025-08-27T18:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"User with id 1 not found\", \"path\": \"/api/v1/loans\" }"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal Server Error"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> loanRequestRouterDocumentation(LoanRequestRouter loanRequestRouter) {
        return loanRequestRouter.routes();
    }

}
