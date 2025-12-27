package co.com.bancolombia.usecase.franchise.usecase.businessoperation;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.usecase.franchise.exception.BusinessException;
import co.com.bancolombia.usecase.franchise.usecase.view.ProductLargestStockByBranch;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;

public class BusinessOperation {

    private BusinessOperation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Mono<ProductModel> updateProductStock(
            ProductModel product,
            Integer newStock,
            FranchisePersistencePort franchisePersistencePort
    ) {
        if (newStock == null || newStock < 0) {
            return Mono.error(new BusinessException(GlobalMessage.BAD_PARAMETER));
        }

        if (product.getStock() != null && product.getStock().equals(newStock)) {
            return Mono.just(product);
        }

        product.setStock(newStock);
        return franchisePersistencePort.saveProduct(product);
    }

    public static Mono<ProductModel> updateMainProductStock(ProductModel product, int stockToSubtract, FranchisePersistencePort franchisePersistencePort) {
        if (product.getStock() < stockToSubtract) {
            return Mono.error(new BusinessException(GlobalMessage.INSUFFICIENT_STOCK));
        }
        product.setStock(product.getStock() - stockToSubtract);
        return franchisePersistencePort.saveProduct(product);
    }

    public static void addOrUpdateBranchProduct(BranchModel branch, String savedProductId, String productName, Integer stock) {
        if (branch.getProducts() == null) {
            branch.setProducts(new ArrayList<>());
        }

        var existingProduct = branch.getProducts().stream()
                .filter(p -> savedProductId.equals(p.getId()))
                .findFirst();

        if (existingProduct.isPresent()) {
            var p = existingProduct.get();
            p.setStock((p.getStock() == null ? 0 : p.getStock()) + stock);
        } else {
            branch.getProducts().add(new ProductModel(savedProductId, productName, stock, null));
        }
    }

    public static Mono<ProductLargestStockByBranch> findLargestStockProduct(BranchModel branch) {
        return Mono.justOrEmpty(branch.getProducts())
                .filter(products -> !products.isEmpty())
                .map(products -> products.stream()
                        .max(Comparator.comparingInt(ProductModel::getStock))
                        .orElseThrow()
                )
                .map(product -> new ProductLargestStockByBranch(
                        branch.getId(),
                        branch.getName(),
                        product.getId(),
                        product.getName(),
                        product.getStock()
                ));
    }

    public static void validateSameFranchise(BranchModel branch, ProductModel product) {
        if (!branch.getFranchiseId().equals(product.getFranchiseId())) {
            throw new BusinessException(GlobalMessage.BRANCH_PRODUCT_DIFFERENT_FRANCHISE);
        }
    }
}
