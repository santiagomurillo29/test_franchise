package co.com.bancolombia.mongo.health;

import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;
import co.com.bancolombia.mongo.exception.DataBaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class MongoSafeExecutor {

    private final MongoHealthChecker mongoHealthChecker;

    public <T> Mono<T> executeMono(Supplier<Mono<T>> operation) {
        return mongoHealthChecker.isDatabaseUp().flatMap(isUp -> {
            if (!isUp) {
                return Mono.error(new DataBaseException(GlobalMessage.DATABASE_ERROR));
            }
            return operation.get();
        });
    }

    public <T> Flux<T> executeFlux(Supplier<Flux<T>> operation) {
        return mongoHealthChecker.isDatabaseUp().flatMapMany(isUp -> {
            if (!isUp) {
                return Flux.error(new DataBaseException(GlobalMessage.DATABASE_ERROR));
            }
            return operation.get();
        });
    }
}
