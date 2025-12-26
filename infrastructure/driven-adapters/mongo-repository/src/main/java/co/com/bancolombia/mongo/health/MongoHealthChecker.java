package co.com.bancolombia.mongo.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoHealthChecker {

    private final ReactiveMongoDatabaseFactory mongoDatabaseFactory;

    public Mono<Boolean> isDatabaseUp() {
        return Mono.from(mongoDatabaseFactory.getMongoDatabase())
                .flatMap(db -> Mono.from(db.runCommand(new Document("ping", 1))))
                .timeout(Duration.ofSeconds(50))
                .map(result -> true)
                .onErrorResume(e -> {
                    log.error("Ping to MongoDB failed: {}", e.getMessage());
                    return Mono.just(false);
                });
    }
}
