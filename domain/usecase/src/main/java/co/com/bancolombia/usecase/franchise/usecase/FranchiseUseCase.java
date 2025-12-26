package co.com.bancolombia.usecase.franchise.usecase;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.usecase.franchise.exception.BusinessException;
import co.com.bancolombia.usecase.franchise.usecase.api.FranchiseServicePort;
import co.com.bancolombia.usecase.franchise.usecase.businessoperation.BusinessOperation;
import co.com.bancolombia.usecase.franchise.usecase.view.ProductLargestStockByBranch;
import reactor.core.publisher.Flux;
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
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException(GlobalMessage.BAD_PARAMETER));
                    }
                    franchiseModel.setBranches(new ArrayList<>());
                    return franchisePersistencePort.saveFranchise(franchiseModel);
                });
    }

    @Override
    public Mono<BranchModel> createBranch(String idFranchise, BranchModel branchModel) {
        return franchisePersistencePort.findFranchiseById(idFranchise)
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMap(franchise ->
                        franchisePersistencePort.existsBranchByName(branchModel.getName())
                                .flatMap(exists -> {
                                    if (Boolean.TRUE.equals(exists)) {
                                        return Mono.error(new BusinessException(GlobalMessage.BAD_PARAMETER));
                                    }
                                    return franchisePersistencePort.saveBranch(idFranchise, branchModel);
                                })
                );
    }

    @Override
    public Mono<ProductModel> createProduct(ProductModel productModel, String idFranchise) {
        return franchisePersistencePort.findFranchiseById(idFranchise)
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMap(franchise ->
                        franchisePersistencePort.existsProductByName(productModel.getName())
                                .flatMap(exists -> {
                                    if (Boolean.TRUE.equals(exists)) {
                                        return Mono.error(new BusinessException(GlobalMessage.BAD_PARAMETER));
                                    }
                                    productModel.setFranchiseId(franchise.getId());
                                    return franchisePersistencePort.saveProduct(productModel);
                                })
                );
    }

    @Override
    public Mono<BranchModel> addProductToBranch(String idBranch, String productId, Integer stock) {
        return franchisePersistencePort.findBranchById(idBranch)
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMap(branch -> franchisePersistencePort.findProductById(productId)
                        .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                        .flatMap(product -> {
                            BusinessOperation.validateSameFranchise(branch, product);
                            return BusinessOperation.updateMainProductStock(product, stock, franchisePersistencePort)
                                    .flatMap(savedProduct -> {
                                        BusinessOperation.addOrUpdateBranchProduct(
                                                branch, productId, savedProduct.getName(), stock
                                        );
                                        return franchisePersistencePort.updateBranch(branch)
                                                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                                                .thenReturn(branch);
                                    });
                        })
                );
    }

    @Override
    public Mono<ProductModel> updateStockProduct(String idProduct, Integer newStock) {
        return franchisePersistencePort.findProductById(idProduct)
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMap(product ->
                        BusinessOperation.updateProductStock(product, newStock, franchisePersistencePort)
                );
    }

    @Override
    public Flux<ProductLargestStockByBranch> findProductLargestStock(String idFranchise) {
        return franchisePersistencePort.findFranchiseById(idFranchise)
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(BusinessOperation::findLargestStockProduct);
    }

    @Override
    public Mono<Void> deleteProductOfBranch(String idBranch, String idProduct) {
        return franchisePersistencePort.findBranchById(idBranch)
                .switchIfEmpty(Mono.error(new BusinessException(GlobalMessage.NOT_FOUND)))
                .flatMap(branch ->
                        franchisePersistencePort.removeProductFromBranch(idBranch, idProduct)
                )
                .flatMap(deleted -> {
                    if (Boolean.FALSE.equals(deleted)) {
                        return Mono.error(new BusinessException(GlobalMessage.NOT_FOUND));
                    }
                    return Mono.empty();
                });
    }
}
