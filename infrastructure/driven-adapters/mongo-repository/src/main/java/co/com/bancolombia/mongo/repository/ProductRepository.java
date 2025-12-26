package co.com.bancolombia.mongo.repository;

import co.com.bancolombia.mongo.entity.ProductEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String>{
    Mono<Boolean> existsByName(String name);
    Flux<ProductEntity> findByFranchiseId(String franchiseId);
}
