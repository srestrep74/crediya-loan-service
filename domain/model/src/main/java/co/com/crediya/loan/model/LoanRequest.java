package co.com.crediya.loan.model;

import co.com.crediya.loan.model.valueobjects.Email;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import co.com.crediya.loan.model.valueobjects.LoanTerm;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanRequest {
    private Long id;
    private LoanAmount amount;
    private LoanTerm loanTerm;
    private Email email;
    private LoanRequestStatus status;
    private LoanType loanType;
    private Long customerId;

    @Override
    public String toString() {
        return "LoanRequest{" +
                "id=" + id +
                ", amount=" + amount +
                ", loanTerm=" + loanTerm +
                ", email=" + email +
                ", status=" + status +
                ", loanType=" + (loanType != null ? loanType.getName() : "null") +
                ", customerId=" + customerId +
                '}';
    }
}
