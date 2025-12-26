package co.com.bancolombia.usecase.franchise.usecase.api;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<FranchiseModel> createFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> createBranch(String idFranchise, BranchModel branchModel);
    Mono<ProductModel> createProduct(ProductModel productModel, String idFranchise);
    Mono<BranchModel> addProductToBranch(String idBranch, String idProduct, Integer stockProduct);

    Mono<ProductModel> updateStockProduct(String nameProduct, Integer newStockProduct);

    Mono<Void> deleteProductOfBranch (String idBranch, String idProduct);
}
