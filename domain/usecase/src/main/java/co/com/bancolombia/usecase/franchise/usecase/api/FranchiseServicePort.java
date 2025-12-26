package co.com.bancolombia.usecase.franchise.usecase.api;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<FranchiseModel> createFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> createBranch(String idFranchise, BranchModel branchModel);

}
