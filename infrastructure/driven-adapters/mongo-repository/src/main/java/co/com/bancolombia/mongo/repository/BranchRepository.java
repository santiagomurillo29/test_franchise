package co.com.bancolombia.mongo.repository;

import co.com.bancolombia.mongo.entity.BranchEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveMongoRepository<BranchEntity, String> {
    Mono<Boolean> existsByName(String name);

    Flux<BranchEntity> findByFranchiseId(String idFranchise);
}
