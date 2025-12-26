package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> saveBranch(String idFranchise, BranchModel branchModel);
    Mono<ProductModel> saveProduct(ProductModel productModel);

    Mono<BranchModel> updateBranch(BranchModel branchModel);

    Mono<FranchiseModel> findFranchiseById(String idFranchise);
    Mono<BranchModel> findBranchById(String idBranch);
    Mono<ProductModel> findProductById(String idProduct);

    Mono<Boolean> existsFranchiseByName(String name);
    Mono<Boolean> existsBranchByName(String name);
    Mono<Boolean> existsProductByName(String name);
}
