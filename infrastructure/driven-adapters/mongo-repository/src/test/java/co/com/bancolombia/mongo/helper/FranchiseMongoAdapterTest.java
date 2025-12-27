package co.com.bancolombia.mongo.helper;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.mongo.adapter.FranchiseAdapterMongo;
import co.com.bancolombia.mongo.entity.BranchEntity;
import co.com.bancolombia.mongo.entity.FranchiseEntity;
import co.com.bancolombia.mongo.entity.ProductEntity;
import co.com.bancolombia.mongo.health.MongoSafeExecutor;
import co.com.bancolombia.mongo.mapper.FranchiseMapperMongo;
import co.com.bancolombia.mongo.repository.BranchRepository;
import co.com.bancolombia.mongo.repository.FranchiseRepository;
import co.com.bancolombia.mongo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class FranchiseMongoAdapterTest {

    @Mock private FranchiseRepository franchiseRepository;
    @Mock private BranchRepository branchRepository;
    @Mock private ProductRepository productRepository;
    @Mock private FranchiseMapperMongo franchiseMapperMongo;
    @Mock private MongoSafeExecutor mongoSafeExecutor;

    private FranchiseAdapterMongo adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchiseAdapterMongo(
                franchiseRepository,
                branchRepository,
                productRepository,
                franchiseMapperMongo,
                mongoSafeExecutor
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveFranchise_persistsAndMaps() {
        FranchiseModel model = new FranchiseModel("1S", "Alkomprar", null);
        FranchiseEntity entity = new FranchiseEntity("1S", "Alkomprar");

        when(franchiseMapperMongo.toEntityFranchise(model)).thenReturn(entity);
        when(franchiseRepository.save(entity)).thenReturn(Mono.just(entity));
        when(franchiseMapperMongo.toModelFranchise(entity)).thenReturn(model);

        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation -> ((Supplier<Mono<FranchiseModel>>) invocation.getArgument(0)).get());

        create(adapter.saveFranchise(model))
                .expectNextMatches(result -> result.getId().equals("1S"))
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveBranch_persistsAndMaps() {
        String idFranchise = "FR001";

        BranchModel model = new BranchModel("1S", "Alkomprar Medellin", null, null);
        BranchEntity entity = new BranchEntity("1S", "Alkomprar Medellin", null, null);

        when(franchiseMapperMongo.toEntityBranch(model)).thenReturn(entity);
        when(branchRepository.save(entity)).thenReturn(Mono.just(entity));
        when(franchiseMapperMongo.toModelBranch(entity)).thenReturn(model);
        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation ->
                        ((Supplier<Mono<BranchModel>>) invocation.getArgument(0)).get()
                );

        StepVerifier.create(adapter.saveBranch(idFranchise, model))
                .expectNextMatches(result ->
                        result.getId().equals("1S") &&
                                result.getName().equals("Alkomprar Medellin")
                )
                .verifyComplete();
    }


    @Test
    @SuppressWarnings("unchecked")
    void saveProduct_persistsAndMaps() {
        ProductModel model = new ProductModel("1S", "Laptop Dell", null, null);
        ProductEntity entity = new ProductEntity("1S", "Laptop Dell", null, null);

        when(franchiseMapperMongo.toEntityProduct(model)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(Mono.just(entity));
        when(franchiseMapperMongo.toModelProduct(entity)).thenReturn(model);

        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation -> ((Supplier<Mono<FranchiseModel>>) invocation.getArgument(0)).get());

        create(adapter.saveProduct(model))
                .expectNextMatches(result -> result.getId().equals("1S"))
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findFranchiseById_returnsMappedFranchiseWithBranches() {
        String franchiseId = "FR001";

        FranchiseEntity franchiseEntity = new FranchiseEntity();
        franchiseEntity.setId(franchiseId);
        franchiseEntity.setName("Alkomprar");

        BranchEntity branch1 = new BranchEntity("B001", "Branch 1", franchiseId, new ArrayList<>());
        BranchEntity branch2 = new BranchEntity("B002", "Branch 2", franchiseId, new ArrayList<>());

        List<BranchEntity> branchEntities = List.of(branch1, branch2);

        BranchModel branchModel1 = new BranchModel("B001", "Branch 1", null,  new ArrayList<>());
        BranchModel branchModel2 = new BranchModel("B002", "Branch 2", null,  new ArrayList<>());

        List<BranchModel> branchModels = List.of(branchModel1, branchModel2);

        FranchiseModel franchiseModel = new FranchiseModel();
        franchiseModel.setId(franchiseId);
        franchiseModel.setName("Alkomprar");

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchiseEntity));
        when(branchRepository.findByFranchiseId(franchiseId)).thenReturn(Flux.fromIterable(branchEntities));
        when(franchiseMapperMongo.toModelFranchise(franchiseEntity)).thenReturn(franchiseModel);
        when(franchiseMapperMongo.toModelBranchList(branchEntities)).thenReturn(branchModels);
        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation ->
                        ((Supplier<Mono<FranchiseModel>>) invocation.getArgument(0)).get()
                );

        StepVerifier.create(adapter.findFranchiseById(franchiseId))
                .assertNext(result -> {
                    assertEquals(franchiseId, result.getId());
                    assertEquals(2, result.getBranches().size());
                    assertEquals("B001", result.getBranches().get(0).getId());
                    assertEquals("B002", result.getBranches().get(1).getId());
                })
                .verifyComplete();
    }



    @Test
    @SuppressWarnings("unchecked")
    void findBranchById_persistsAndMaps() {
        BranchModel model = new BranchModel("1S", "Alkomprar Medellin", null, null);
        BranchEntity entity = new BranchEntity("1S", "Alkomprar Medeliin", null, null);

        when(branchRepository.findById("1S")).thenReturn(Mono.just(entity));
        when(franchiseMapperMongo.toModelBranch(entity)).thenReturn(model);

        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation -> ((Supplier<Mono<FranchiseModel>>) invocation.getArgument(0)).get());

        create(adapter.findBranchById(model.getId()))
                .expectNextMatches(result -> result.getId().equals("1S"))
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findProductById_persistsAndMaps() {
        ProductModel model = new ProductModel("1S", "Laptop Dell", null, null);
        ProductEntity entity = new ProductEntity("1S", "Laptop Dell", null, null);

        when(productRepository.findById("1S")).thenReturn(Mono.just(entity));
        when(franchiseMapperMongo.toModelProduct(entity)).thenReturn(model);

        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation -> ((Supplier<Mono<ProductModel>>) invocation.getArgument(0)).get());

        StepVerifier.create(adapter.findProductById(model.getId()))
                .expectNextMatches(result -> result.getId().equals("1S"))
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void removeProductFromBranch_whenProductExists_returnsTrue() {
        String branchId = "BR001";
        String productId = "PR001";

        when(branchRepository.removeProductFromBranch(branchId, productId))
                .thenReturn(Mono.just(1L));

        when(mongoSafeExecutor.executeMono(any()))
                .thenAnswer(invocation ->
                        ((Supplier<Mono<Boolean>>) invocation.getArgument(0)).get()
                );

        StepVerifier.create(adapter.removeProductFromBranch(branchId, productId))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findProductsByFranchiseId_returnsMappedProducts() {
        String franchiseId = "FR001";

        ProductEntity entity1 = new ProductEntity("PR001", "Laptop", 50, franchiseId);
        ProductEntity entity2 = new ProductEntity("PR002", "Mouse", 100, franchiseId);

        ProductModel model1 = new ProductModel("PR001", "Laptop", 50, franchiseId);
        ProductModel model2 = new ProductModel("PR002", "Mouse", 100, franchiseId);

        when(productRepository.findByFranchiseId(franchiseId)).thenReturn(Flux.just(entity1, entity2));
        when(franchiseMapperMongo.toModelProduct(entity1)).thenReturn(model1);
        when(franchiseMapperMongo.toModelProduct(entity2)).thenReturn(model2);

        when(mongoSafeExecutor.executeFlux(any()))
                .thenAnswer(invocation ->
                        ((Supplier<Flux<ProductModel>>) invocation.getArgument(0)).get()
                );

        StepVerifier.create(adapter.findProductsByFranchiseId(franchiseId))
                .expectNext(model1)
                .expectNext(model2)
                .verifyComplete();
    }

    @Test
    void existsFranchiseByName_shouldReturnTrue() {
        String name = "Test Franchise";

        when(franchiseRepository.existsByName(name)).thenReturn(Mono.just(true));
        when(mongoSafeExecutor.executeMono(ArgumentMatchers.<Supplier<Mono<Boolean>>>any()))
                .thenAnswer(invocation -> invocation.<Supplier<Mono<Boolean>>>getArgument(0).get());


        create(adapter.existsFranchiseByName(name))
                .expectNext(true)
                .verifyComplete();

        verify(franchiseRepository).existsByName(name);
    }

    @Test
    void existsProductByName_shouldReturnTrue() {
        String name = "Laptop Dell";

        when(productRepository.existsByName(name)).thenReturn(Mono.just(true));
        when(mongoSafeExecutor.executeMono(ArgumentMatchers.<Supplier<Mono<Boolean>>>any()))
                .thenAnswer(invocation -> invocation.<Supplier<Mono<Boolean>>>getArgument(0).get());

        create(adapter.existsProductByName(name))
                .expectNext(true)
                .verifyComplete();

        verify(productRepository).existsByName(name);
    }

    @Test
    void existsBranchByName_shouldReturnTrue() {
        String branchName = "Sucursal Norte";

        when(branchRepository.existsByName(branchName)).thenReturn(Mono.just(true));
        when(mongoSafeExecutor.executeMono(ArgumentMatchers.<Supplier<Mono<Boolean>>>any()))
                .thenAnswer(invocation -> invocation.<Supplier<Mono<Boolean>>>getArgument(0).get());

        create(adapter.existsBranchByName(branchName))
                .expectNext(true)
                .verifyComplete();

        verify(branchRepository).existsByName(branchName);
    }
}
