package co.com.crediya.loan.model;

import co.com.crediya.loan.model.valueobjects.InterestRate;
import co.com.crediya.loan.model.valueobjects.MoneyRange;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {
    private Long id;
    private String name;
    private MoneyRange moneyRange;
    private InterestRate interestRate;
    private boolean autoValidation;
}
