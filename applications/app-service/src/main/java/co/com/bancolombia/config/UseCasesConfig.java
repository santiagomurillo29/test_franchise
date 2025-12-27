package co.com.bancolombia.config;

import co.com.bancolombia.model.franchise.gateways.FranchisePersistencePort;
import co.com.bancolombia.mongo.adapter.FranchiseAdapterMongo;
import co.com.bancolombia.mongo.health.MongoSafeExecutor;
import co.com.bancolombia.mongo.mapper.FranchiseMapperMongo;
import co.com.bancolombia.mongo.repository.BranchRepository;
import co.com.bancolombia.mongo.repository.FranchiseRepository;
import co.com.bancolombia.mongo.repository.ProductRepository;
import co.com.bancolombia.usecase.franchise.usecase.FranchiseUseCase;
import co.com.bancolombia.usecase.franchise.usecase.api.FranchiseServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
        private final MongoSafeExecutor mongoSafeExecutor;
        private final FranchiseRepository franchiseRepository;
        private final BranchRepository branchRepository;
        private final ProductRepository productRepository;
        private final FranchiseMapperMongo franchiseMapperMongo;

        @Bean
        public FranchisePersistencePort franchisePersistencePort(){
                return new FranchiseAdapterMongo(
                        franchiseRepository,
                        branchRepository,
                        productRepository,
                        franchiseMapperMongo,
                        mongoSafeExecutor);
        }

        @Bean
        public FranchiseServicePort franchiseServicePort(){
                return new FranchiseUseCase(franchisePersistencePort());
        }
}
