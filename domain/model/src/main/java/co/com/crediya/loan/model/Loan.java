package co.com.crediya.loan.model;

import co.com.crediya.loan.model.valueobjects.Email;
import co.com.crediya.loan.model.valueobjects.LoanAmount;
import co.com.crediya.loan.model.valueobjects.LoanTerm;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Loan {
    private Long id;
    private LoanAmount amount;
    private LoanTerm loanTerm;
    private Email email;
    private LoanRequestStatus status;
    private LoanType loanType;
}
