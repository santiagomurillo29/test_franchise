package co.com.bancolombia.mongo.adapter;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.mongo.health.MongoSafeExecutor;
import co.com.bancolombia.mongo.mapper.FranchiseMapperMongo;
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
    public Mono<Boolean> existsFranchiseByName(String nameFranchise) {
        return mongoSafeExecutor.executeMono(() ->
                franchiseRepository.existsByName(nameFranchise)
                        .doOnSubscribe(sub -> log.info("Checking existence of franchise: {}", nameFranchise))
                        .doOnSuccess(result -> log.info("The franchise does exist: {}", result))
                        .doOnError(e -> log.error("Error checking if the franchise exists by name {}: {}", nameFranchise, e.getMessage()))
        );
    }
}
