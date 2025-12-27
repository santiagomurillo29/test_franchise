package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.usecase.franchise.usecase.FranchiseUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @Mock
    FranchisePersistencePort repo;

    @InjectMocks
    FranchiseUseCase useCase;

    @Test
    void createFranchise_whenNotExists_thenSave() {
        FranchiseModel model = new FranchiseModel("1S", "Alkomprar", null);

        given(repo.existsFranchiseByName(model.getName())).willReturn(Mono.just(false));
        given(repo.saveFranchise(model)).willReturn(Mono.just(model));

        StepVerifier.create(useCase.createFranchise(model))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void createBranch_whenNotExists_thenSave() {
        String franchiseId = "1F";

        BranchModel branchModel = new BranchModel("1S", "Alkomprar Medellin", null, new ArrayList<>());

        FranchiseModel existingFranchise = new FranchiseModel("1F", "franchiseName", new ArrayList<>());

        given(repo.findFranchiseById(franchiseId)).willReturn(Mono.just(existingFranchise));
        given(repo.existsBranchByName(branchModel.getName())).willReturn(Mono.just(false));
        given(repo.saveBranch(franchiseId, branchModel)).willReturn(Mono.just(branchModel));

        StepVerifier.create(useCase.createBranch(franchiseId, branchModel))
                .expectNext(branchModel)
                .verifyComplete();
    }


    @Test
    void createProduct_whenNotExists_thenSave() {
        String franchiseId = "1F";
        ProductModel productModel = new ProductModel("1P", "Laptop Dell", 1, null);
        FranchiseModel existingFranchise = new FranchiseModel("1F", "franchiseName", new ArrayList<>());

        given(repo.findFranchiseById(franchiseId)).willReturn(Mono.just(existingFranchise));
        given(repo.existsProductByName(productModel.getName())).willReturn(Mono.just(false));
        given(repo.saveProduct(productModel)).willReturn(Mono.just(productModel));

        StepVerifier.create(useCase.createProduct(productModel, franchiseId))
                .expectNext(productModel)
                .verifyComplete();
    }

    @Test
    void addProductToBranch_whenAllIsValid_thenSuccess() {
        String branchId = "1B";
        String productId = "1P";
        String franchiseId = "1F";
        Integer stockToAdd = 5;

        BranchModel branch = new BranchModel(branchId, "Alkomprar Medellín", franchiseId, new ArrayList<>());
        ProductModel dbProduct = new ProductModel(productId, "Laptop Dell", 10, franchiseId);
        ProductModel savedProduct = new ProductModel(productId, "Laptop Dell", 5, franchiseId);

        given(repo.findBranchById(branchId)).willReturn(Mono.just(branch));
        given(repo.findProductById(productId)).willReturn(Mono.just(dbProduct));
        given(repo.saveProduct(any(ProductModel.class))).willReturn(Mono.just(savedProduct));
        given(repo.updateBranch(any(BranchModel.class))).willReturn(Mono.just(branch));

        StepVerifier.create(useCase.addProductToBranch(branchId, productId, stockToAdd))
                .assertNext(result -> {
                    assertEquals(branchId, result.getId());
                    assertEquals(1, result.getProducts().size());
                    ProductModel p = result.getProducts().get(0);
                    assertEquals(productId, p.getId());
                    assertEquals("Laptop Dell", p.getName());
                    assertEquals(stockToAdd, p.getStock());
                })
                .verifyComplete();
    }

    @Test
    void updateNameFranchise_whenValid_thenSuccess() {
        String franchiseId = "1F";
        String oldName = "Franquicia A";
        String newName = "Franquicia B";

        FranchiseModel franchise = new FranchiseModel(franchiseId, oldName, new ArrayList<>());
        FranchiseModel updatedFranchise = new FranchiseModel(franchiseId, newName, new ArrayList<>());

        given(repo.findFranchiseById(franchiseId)).willReturn(Mono.just(franchise));
        given(repo.existsFranchiseByName(newName)).willReturn(Mono.just(false));
        given(repo.saveFranchise(any())).willReturn(Mono.just(updatedFranchise));

        StepVerifier.create(useCase.updateNameFranchise(franchiseId, newName))
                .expectNextMatches(updated -> updated.getName().equals(newName))
                .verifyComplete();

        then(repo).should().saveFranchise(argThat(fr -> fr.getName().equals(newName)));
    }

    @Test
    void updateNameBranch_whenValid_thenSuccess() {
        String branchId = "1B";
        String oldName = "Sucursal A";
        String newName = "Sucursal B";

        BranchModel branch = new BranchModel(branchId, oldName, null, new ArrayList<>());
        BranchModel updatedBranch = new BranchModel(branchId, newName, null, new ArrayList<>());

        given(repo.findBranchById(branchId)).willReturn(Mono.just(branch));
        given(repo.existsBranchByName(newName)).willReturn(Mono.just(false));
        given(repo.updateBranch(any(BranchModel.class))).willReturn(Mono.just(updatedBranch));

        StepVerifier.create(useCase.updateNameBranch(branchId, newName))
                .expectNextMatches(result -> result.getName().equals(newName))
                .verifyComplete();

        then(repo).should()
                .updateBranch(argThat(br -> br.getName().equals(newName)));
    }

    @Test
    void updateNameProduct_whenValid_thenSuccess() {
        String productId = "1P";
        String oldName = "Producto A";
        String newName = "Producto B";

        ProductModel product = new ProductModel(productId, oldName, 10, null);
        ProductModel updatedProduct = new ProductModel(productId, newName, 10, null);

        given(repo.findProductById(productId)).willReturn(Mono.just(product));
        given(repo.existsProductByName(newName)).willReturn(Mono.just(false));
        given(repo.saveProduct(any())).willReturn(Mono.just(updatedProduct));
        given(repo.updateProductNameInBranches(productId, newName)).willReturn(Mono.empty());

        StepVerifier.create(useCase.updateNameProduct(productId, newName))
                .expectNextMatches(result -> result.getName().equals(newName))
                .verifyComplete();

        then(repo).should()
                .saveProduct(argThat(p -> p.getName().equals(newName)));

        then(repo).should()
                .updateProductNameInBranches(productId, newName);
    }

    @Test
    void updateStockProduct_whenValid_thenSuccess() {
        String productId = "1P";
        int newStock = 50;

        ProductModel product = new ProductModel(productId, "Producto A", 10, null);
        ProductModel updatedProduct = new ProductModel(productId, "Producto A", newStock, null);

        given(repo.findProductById(productId)).willReturn(Mono.just(product));
        given(repo.saveProduct(any())).willReturn(Mono.just(updatedProduct));

        StepVerifier.create(useCase.updateStockProduct(productId, newStock))
                .expectNextMatches(result -> result.getStock() == newStock)
                .verifyComplete();

        then(repo).should()
                .saveProduct(argThat(p -> p.getStock() == newStock));
    }

    @Test
    void findProductsByFranchiseId_whenValid_thenSuccess() {
        String franchiseId = "1F";
        String franchiseName = "Franquicia A";

        FranchiseModel franchise = new FranchiseModel(franchiseId, franchiseName, new ArrayList<>());

        ProductModel product1 = new ProductModel("1P", "Producto A", 10, franchiseId);
        ProductModel product2 = new ProductModel("2P", "Producto B", 20, franchiseId);
        ProductModel product3 = new ProductModel("3P", "Producto C", 15, franchiseId);

        given(repo.findFranchiseById(franchiseId)).willReturn(Mono.just(franchise));
        given(repo.findProductsByFranchiseId(franchiseId)).willReturn(Flux.just(product1, product2, product3));

        StepVerifier.create(useCase.findProductsByFranchiseId(franchiseId))
                .expectNext(product1)
                .expectNext(product2)
                .expectNext(product3)
                .verifyComplete();
    }

    @Test
    void findProductLargestStock_whenValid_thenSuccess() {
        String franchiseId = "1F";
        String franchiseName = "Franquicia A";
        String branchId = "1B";
        String branchName = "Sucursal A";

        ProductModel prod1 = new ProductModel("1", "Producto A", 50, franchiseId);
        ProductModel prod2 = new ProductModel("2", "Producto B", 80, franchiseId);
        ProductModel prod3 = new ProductModel("3", "Producto C", 30, franchiseId);

        BranchModel branch = new BranchModel(branchId, branchName, null, List.of(prod1, prod2, prod3));
        FranchiseModel franchise = new FranchiseModel(franchiseId, franchiseName, List.of(branch));

        given(repo.findFranchiseById(franchiseId)).willReturn(Mono.just(franchise));

        StepVerifier.create(useCase.findProductLargestStock(franchiseId))
                .expectNextMatches(result ->
                        result.getBranchId().equals(branchId)
                                && result.getProductId().equals("2")
                                && result.getStock() == 80
                )
                .verifyComplete();
    }

    @Test
    void deleteProductOfBranch_whenValid_thenSuccess() {
        String branchId = "1B";
        String productId = "2";

        BranchModel branch = new BranchModel(branchId, "Sucursal A", null,  new ArrayList<>());

        given(repo.findBranchById(branchId)).willReturn(Mono.just(branch));
        given(repo.removeProductFromBranch(branchId, productId)).willReturn(Mono.just(true));

        StepVerifier.create(useCase.deleteProductOfBranch(branchId, productId))
                .verifyComplete();

        then(repo).should()
                .removeProductFromBranch(branchId, productId);
    }
}
