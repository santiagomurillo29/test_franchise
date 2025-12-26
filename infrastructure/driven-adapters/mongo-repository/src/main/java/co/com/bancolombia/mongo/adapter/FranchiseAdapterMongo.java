package co.com.bancolombia.mongo.adapter;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.mongo.health.MongoSafeExecutor;
import co.com.bancolombia.mongo.mapper.FranchiseMapperMongo;
import co.com.bancolombia.mongo.repository.BranchRepository;
import co.com.bancolombia.mongo.repository.FranchiseRepository;
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
}
