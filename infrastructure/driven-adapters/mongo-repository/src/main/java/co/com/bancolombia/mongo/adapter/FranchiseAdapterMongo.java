package co.com.bancolombia.mongo.adapter;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.mongo.health.MongoSafeExecutor;
import co.com.bancolombia.mongo.mapper.FranchiseMapperMongo;
import co.com.bancolombia.mongo.repository.BranchRepository;
import co.com.bancolombia.mongo.repository.FranchiseRepository;
import co.com.bancolombia.mongo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FranchiseAdapterMongo implements FranchisePersistencePort {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final FranchiseMapperMongo franchiseMapperMongo;
    private final MongoSafeExecutor mongoSafeExecutor;

    @Override
    public Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel) {
        return mongoSafeExecutor.executeMono(() ->
                franchiseRepository.save(franchiseMapperMongo.toEntityFranchise(franchiseModel))
                        .doOnSubscribe(sub -> log.info("Saving franchise: {}", franchiseModel))
                        .map(franchiseMapperMongo::toModelFranchise)
                        .doOnSuccess(saved -> log.info("Franchise saved successfully: {}", saved))
                        .doOnError(e -> log.error("Error saving franchise: {}", e.getMessage()))
        );
    }

    @Override
    public Mono<BranchModel> saveBranch(String idFranchise, BranchModel branchModel) {
        return mongoSafeExecutor.executeMono(() ->
                Mono.just(franchiseMapperMongo.toEntityBranch(branchModel))
                        .map(entity -> {
                            entity.setFranchiseId(idFranchise);
                            return entity;
                        })
                        .flatMap(branchRepository::save)
                        .doOnSubscribe(sub -> log.info("Saving branch for franchise: {}", idFranchise))
                        .map(franchiseMapperMongo::toModelBranch)
                        .doOnSuccess(saved -> log.info("Branch saved successfully: {}", saved.getId()))
                        .doOnError(e -> log.error("Error saving branch", e))
        );
    }

    @Override
    public Mono<ProductModel> saveProduct(ProductModel productModel) {
        return mongoSafeExecutor.executeMono(() ->
                productRepository.save(franchiseMapperMongo.toEntityProduct(productModel))
                        .doOnSubscribe(sub -> log.info("Saving product: {}", productModel))
                        .map(franchiseMapperMongo::toModelProduct)
                        .doOnSuccess(saved -> log.info("Product saved successfully: {}", saved))
                        .doOnError(e -> log.error("Error Saving product: {}", e.getMessage()))
        );
    }

    @Override
    public Mono<BranchModel> updateBranch(BranchModel branchModel) {
        return mongoSafeExecutor.executeMono(() ->
                branchRepository.findById(branchModel.getId())
                        .map(entity -> {
                            entity.setName(branchModel.getName());

                            if (branchModel.getProducts() != null) {
                                entity.setProducts(franchiseMapperMongo.toTupleList(branchModel.getProducts()));
                            }

                            return entity;
                        })
                        .flatMap(branchRepository::save)
                        .doOnSubscribe(sub -> log.info("updating product: {}", branchModel))
                        .map(franchiseMapperMongo::toModelBranch)
                        .doOnSuccess(saved -> log.info("branch update successfully: {}", saved))
                        .doOnError(e -> log.error("Error update branch: {}", e.getMessage()))
        );
    }

    @Override
    public Mono<FranchiseModel> findFranchiseById(String idFranchise) {
        return mongoSafeExecutor.executeMono(() ->
                franchiseRepository.findById(idFranchise)
                        .flatMap(entity ->
                                branchRepository.findByFranchiseId(entity.getId())
                                        .collectList()
                                        .map(branches -> {
                                            FranchiseModel model = franchiseMapperMongo.toModelFranchise(entity);
                                            model.setBranches(franchiseMapperMongo.toModelBranchList(branches));
                                            return model;
                                        })
                        )
        );
    }

    @Override
    public Mono<BranchModel> findBranchById(String idBranch) {
        return mongoSafeExecutor.executeMono(() ->
                branchRepository.findById(idBranch)
                        .doOnSubscribe(sub -> log.info("Finding branch with id: {}", idBranch))
                        .map(franchiseMapperMongo::toModelBranch)
                        .doOnSuccess(found -> log.info("Branch found: {}", found))
                        .doOnError(e -> log.error("Error finding branch with id {}: {}", idBranch, e.getMessage()))
        );
    }

    @Override
    public Mono<ProductModel> findProductById(String idProduct) {
        return mongoSafeExecutor.executeMono(() ->
                productRepository.findById(idProduct)
                        .doOnSubscribe(sub -> log.info("Finding product with id: {}", idProduct))
                        .map(franchiseMapperMongo::toModelProduct)
                        .doOnSuccess(found -> log.info("Product found with id: {}", found))
                        .doOnError(e -> log.error("Error finding product by id {}: {}", idProduct, e.getMessage()))
                        .switchIfEmpty(Mono.empty())
        );
    }

    @Override
    public Mono<Boolean> existsFranchiseByName(String nameFranchise) {
        return mongoSafeExecutor.executeMono(() ->
                franchiseRepository.existsByName(nameFranchise)
                        .doOnSubscribe(sub -> log.info("Checking existence of franchise: {}", nameFranchise))
                        .doOnSuccess(result -> log.info("The franchise does exist: {}", result))
                        .doOnError(e -> log.error("Error checking if the franchise exists by name {}: {}", nameFranchise, e.getMessage()))
        );
    }

    @Override
    public Mono<Boolean> existsBranchByName(String nameBranch) {
        return mongoSafeExecutor.executeMono(() ->
                branchRepository.existsByName(nameBranch)
                        .doOnSubscribe(sub -> log.info("Checking existence of branch: {}", nameBranch))
                        .doOnSuccess(found -> log.info("The Branch does exist: {}", found))
                        .doOnError(e -> log.error("Error checking if the branch exists by name {}: {}", nameBranch, e.getMessage()))
        );
    }

    @Override
    public Mono<Boolean> existsProductByName(String nameProduct) {
        return mongoSafeExecutor.executeMono(() ->
                productRepository.existsByName(nameProduct)
                        .doOnSubscribe(sub -> log.info("Checking existence of product: {}", nameProduct))
                        .doOnSuccess(franchise -> log.info("The product does exist: {}", franchise))
                        .doOnError(e -> log.error("Error checking if the product exists by name {}: {}", nameProduct, e.getMessage()))
        );
    }
}
