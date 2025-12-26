package co.com.bancolombia.usecase.franchise.usecase.businessoperation;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.usecase.franchise.exception.BusinessException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

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

    public static void addOrUpdateBranchProduct(BranchModel branch, String productId, String productName, Integer stock) {
        if (branch.getProducts() == null) {
            branch.setProducts(new ArrayList<>());
        }

        var existingProduct = branch.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        if (existingProduct.isPresent()) {
            var p = existingProduct.get();
            p.setStock(p.getStock() + stock);
        } else {
            branch.getProducts().add(new ProductModel(productId, productName, stock, null));
        }
    }

    public static void validateSameFranchise(BranchModel branch, ProductModel product) {
        if (!branch.getFranchiseId().equals(product.getFranchiseId())) {
            throw new BusinessException(GlobalMessage.BRANCH_PRODUCT_DIFFERENT_FRANCHISE);
        }
    }
}
