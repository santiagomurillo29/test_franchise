package co.com.bancolombia.usecase.franchise.usecase.api;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.usecase.franchise.usecase.view.ProductLargestStockByBranch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<FranchiseModel> createFranchise(FranchiseModel franchiseModel);
    Mono<BranchModel> createBranch(String idFranchise, BranchModel branchModel);
    Mono<ProductModel> createProduct(ProductModel productModel, String idFranchise);
    Mono<BranchModel> addProductToBranch(String idBranch, String idProduct, Integer stockProduct);

    Mono<ProductModel> updateStockProduct(String nameProduct, Integer newStockProduct);
    Mono<FranchiseModel> updateNameFranchise(String idFranchise, String newNameFranchise);
    Mono<BranchModel> updateNameBranch(String nameBranch, String newNameBranch);
    Mono<ProductModel> updateNameProduct(String nameProduct, String newNameProduct);

    Mono<FranchiseModel> findFranchiseById(String idFranchise);
    Mono<BranchModel> findBranchById(String idBranch);
    Flux<ProductModel> findProductsByFranchiseId(String idFranchise);
    Flux<ProductLargestStockByBranch> findProductLargestStock(String idFranchise);

    Mono<Void> deleteProductOfBranch (String idBranch, String idProduct);
}
