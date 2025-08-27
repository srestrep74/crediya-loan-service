package co.com.crediya.loan.r2dbc.adapters.loanrequest;

import co.com.crediya.loan.model.LoanRequest;
import co.com.crediya.loan.model.gateways.LoanRequestReactivePersistenceGateway;
import co.com.crediya.loan.r2dbc.entity.LoanRequestEntity;
import co.com.crediya.loan.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.loan.r2dbc.mapper.LoanRequestMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcLoanRequestReactivePersistenceAdapter extends ReactiveAdapterOperations<
        LoanRequest, LoanRequestEntity, Long, LoanRequestReactiveRepository
> implements LoanRequestReactivePersistenceGateway {

    private final LoanRequestMapper loanRequestMapper;

    public R2dbcLoanRequestReactivePersistenceAdapter(LoanRequestReactiveRepository repository, ObjectMapper mapper, LoanRequestMapper loanRequestMapper){
        super(repository, mapper, entity -> mapper.map(entity, LoanRequest.class));
        this.loanRequestMapper = loanRequestMapper;
    }

    @Override
    public Mono<LoanRequest> save(LoanRequest loanRequest) {
        return super.save(loanRequest);
    }

    @Override
    public LoanRequestEntity toData(LoanRequest loanRequest) {
        return loanRequestMapper.toData(loanRequest);
    }

    @Override
    public LoanRequest toEntity(LoanRequestEntity loanRequestEntity) {
        return loanRequestMapper.toEntity(loanRequestEntity);
    }
}
