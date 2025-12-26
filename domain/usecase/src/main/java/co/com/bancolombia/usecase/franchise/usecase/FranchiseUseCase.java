package co.com.bancolombia.usecase.franchise.usecase;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.usecase.franchise.exception.BusinessException;
import co.com.bancolombia.usecase.franchise.usecase.api.FranchiseServicePort;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class FranchiseUseCase implements FranchiseServicePort {

    private final FranchisePersistencePort franchisePersistencePort;

    public FranchiseUseCase(FranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    @Override
    public Mono<FranchiseModel> createFranchise(FranchiseModel franchiseModel) {
        return franchisePersistencePort.existsFranchiseByName(franchiseModel.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException(GlobalMessage.BAD_PARAMETER));
                    }
                    franchiseModel.setBranches(new ArrayList<>());
                    return franchisePersistencePort.saveFranchise(franchiseModel);
                });
    }
}
