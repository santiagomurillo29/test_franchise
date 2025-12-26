package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> saveBranch(String idFranchise, BranchModel branchModel);

    Mono<FranchiseModel> findFranchiseById(String idFranchise);

    Mono<Boolean> existsFranchiseByName(String name);
    Mono<Boolean> existsBranchByName(String name);
}
