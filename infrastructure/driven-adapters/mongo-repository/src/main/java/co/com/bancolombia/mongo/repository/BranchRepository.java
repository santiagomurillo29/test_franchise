package co.com.bancolombia.mongo.repository;

import co.com.bancolombia.mongo.entity.BranchEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveMongoRepository<BranchEntity, String> {
    Mono<Boolean> existsByName(String name);

    Flux<BranchEntity> findByFranchiseId(String idFranchise);

    @Query("{ '_id': ?0 }")
    @Update("{ '$pull': { 'products': { 'productId': ?1 } } }")
    Mono<Long> removeProductFromBranch(String branchId, String productId);
}
