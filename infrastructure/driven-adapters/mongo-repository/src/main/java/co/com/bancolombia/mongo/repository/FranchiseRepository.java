package co.com.bancolombia.mongo.repository;

import co.com.bancolombia.mongo.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveMongoRepository<FranchiseEntity, String> {
    Mono<Boolean> existsByName(String name);
    Mono<FranchiseEntity> findByName(String name);
    // Mono<FranchiseEntity> findByBranchesId(String idBranch);
}
