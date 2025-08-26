package co.com.crediya.loan.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "loan_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanRequestEntity {

    @Id
    @Column("loan_id")
    private Long id;

    private BigDecimal amount;
    private Integer loanTerm;
    private String email;

    @Column("state_id")
    private Long statusId;

    @Column("loan_type_id")
    private Long loanTypeId;

    @Column("customer_id")
    private Long customerId;
}
