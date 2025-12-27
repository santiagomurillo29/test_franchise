package co.com.bancolombia.config;

import co.com.bancolombia.mongo.health.MongoSafeExecutor;
import co.com.bancolombia.mongo.mapper.FranchiseMapperMongo;
import co.com.bancolombia.mongo.repository.BranchRepository;
import co.com.bancolombia.mongo.repository.FranchiseRepository;
import co.com.bancolombia.mongo.repository.ProductRepository;
import co.com.bancolombia.usecase.franchise.usecase.api.FranchiseServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UseCasesConfigTest {

    @MockitoBean
    private MongoSafeExecutor mongoSafeExecutor;
    @MockitoBean
    private FranchiseRepository franchiseRepository;
    @MockitoBean
    private BranchRepository branchRepository;
    @MockitoBean
    private FranchiseMapperMongo franchiseMapperMongo;
    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private FranchiseServicePort franchiseServicePort;

    @Test
    void shouldLoadFranchiseServicePortBean() {
        assertThat(franchiseServicePort).isNotNull();
    }
}