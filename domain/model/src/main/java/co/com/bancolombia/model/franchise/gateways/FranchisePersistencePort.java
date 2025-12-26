package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.model.FranchiseModel;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel);

    Mono<Boolean> existsFranchiseByName(String name);
}
