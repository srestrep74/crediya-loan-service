package co.com.crediya.loan.r2dbc.adapters.loantype;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.LoanType;
import co.com.crediya.loan.model.gateways.LoanTypeReactivePersistenceGateway;
import co.com.crediya.loan.r2dbc.adapters.loanrequest.LoanRequestReactiveRepository;
import co.com.crediya.loan.r2dbc.entity.LoanTypeEntity;
import co.com.crediya.loan.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.loan.r2dbc.mapper.LoanRequestMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcLoanTypeReactivePersistenceAdapter extends ReactiveAdapterOperations<
        LoanType, LoanTypeEntity, Long, LoanTypeReactiveRepository
        > implements LoanTypeReactivePersistenceGateway {

    public R2dbcLoanTypeReactivePersistenceAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper){
        super(repository, mapper, entity -> mapper.map(entity, LoanType.class));
    }

    @Override
    public Mono<LoanType> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
