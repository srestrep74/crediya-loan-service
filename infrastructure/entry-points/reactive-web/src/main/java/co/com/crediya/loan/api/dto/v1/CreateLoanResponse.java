package co.com.crediya.loan.api.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "CreateLoanResponse",
        description = "Response after creating a new loan request"
)
public record CreateLoanResponse(

        @Schema(
                description = "Unique identifier of the created loan request",
                example = "101"
        )
        Long id,

        @Schema(
                description = "Requested loan amount",
                example = "15000.00"
        )
        BigDecimal amount,

        @Schema(
                description = "Requested loan term in months",
                example = "24"
        )
        Integer loanTerm,

        @Schema(
                description = "Customer email address",
                example = "john.doe@example.com"
        )
        String email,

        @Schema(
                description = "Loan type identifier",
                example = "1"
        )
        Long loanTypeId,

        @Schema(
                description = "Customer identifier (from external user service)",
                example = "1001"
        )
        Long customerId,

        @Schema(
                description = "Current status of the loan request",
                example = "PENDING_REVIEW"
        )
        String status
) {}
