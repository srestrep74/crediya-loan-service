package co.com.crediya.loan.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "loan_requests_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanStatusEntity {

    @Id
    @Column("loan_state_id")
    private Long id;

    private String name;
    private String description;
}
