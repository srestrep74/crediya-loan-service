package co.com.crediya.loan.api.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "CreateLoanRequest",
        description = "Payload required to create a new loan request"
)
public record CreateLoanRequest(

        @Schema(
                description = "Requested loan amount",
                example = "15000.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal amount,

        @Schema(
                description = "Requested loan term in months",
                example = "24",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer loanTerm,

        @Schema(
                description = "Customer email address",
                example = "john.doe@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @Schema(
                description = "Loan type identifier",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long loanTypeId,

        @Schema(
                description = "Customer identifier (coming from external user service)",
                example = "1001",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long customerId
) {}
