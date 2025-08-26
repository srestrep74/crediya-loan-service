package co.com.crediya.loan.r2dbc.adapters.loan;

import co.com.crediya.loan.r2dbc.entity.LoanRequestEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanRequestReactiveRepository extends ReactiveCrudRepository<LoanRequestEntity, Long>, ReactiveQueryByExampleExecutor<LoanRequestEntity> {
}
