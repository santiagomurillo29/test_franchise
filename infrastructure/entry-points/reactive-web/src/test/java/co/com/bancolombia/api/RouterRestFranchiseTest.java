package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.branch.BranchRequestDto;
import co.com.bancolombia.api.dto.request.franchise.FranchiseRequestDto;
import co.com.bancolombia.api.dto.request.product.AddProductToBranchRequestDto;
import co.com.bancolombia.api.dto.request.product.NameProductRequestDto;
import co.com.bancolombia.api.dto.request.product.ProductRequestDto;
import co.com.bancolombia.api.dto.request.product.StockProductRequestDto;
import co.com.bancolombia.api.dto.request.validation.RequestValidator;
import co.com.bancolombia.api.dto.response.branch.BranchFullResponseDto;
import co.com.bancolombia.api.dto.response.branch.BranchResponseDto;
import co.com.bancolombia.api.dto.response.franchise.FranchiseFullResponseDto;
import co.com.bancolombia.api.dto.response.franchise.FranchiseResponseDto;
import co.com.bancolombia.api.dto.response.product.ProductNameResponseDto;
import co.com.bancolombia.api.dto.response.product.ProductResponseDto;
import co.com.bancolombia.api.handler.HandlerFranchise;
import co.com.bancolombia.api.mapper.FranchiseMapper;
import co.com.bancolombia.api.router.RouterRestFranchise;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import co.com.bancolombia.usecase.franchise.usecase.api.FranchiseServicePort;
import co.com.bancolombia.usecase.franchise.usecase.view.ProductLargestStockByBranch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRestFranchise.class, HandlerFranchise.class})
@WebFluxTest
class RouterRestFranchiseTest {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private FranchiseServicePort service;

    @MockitoBean
    private RequestValidator validator;

    @MockitoBean
    private FranchiseMapper mapper;

    @Test
    void createFranchise_endpoint() {
        FranchiseRequestDto request = new FranchiseRequestDto("Alkomprar");
        FranchiseModel model = new FranchiseModel("1S", "Alkomprar", null);
        FranchiseFullResponseDto response = new FranchiseFullResponseDto("1S", "Alkomprar", null);

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(mapper.toModelFranchise(any())).willReturn(model);
        given(service.createFranchise(any())).willReturn(Mono.just(model));
        given(mapper.toDtoFullFranchise(model)).willReturn(response);

        client.post().uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseFullResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void addBranchToFranchise_endpoint() {
        String idFranchise = "FR001";
        BranchRequestDto request = new BranchRequestDto("Sucursal Medellín");
        BranchModel model = new BranchModel("BR001", "Sucursal Medellín", null, null);
        BranchFullResponseDto response = new BranchFullResponseDto("BR001", "Sucursal Medellín", null);

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(mapper.toModelBranch(any())).willReturn(model);
        given(service.createBranch(idFranchise, model)).willReturn(Mono.just(model));
        given(mapper.toDtoFullBranch(any())).willReturn(response);

        client.post()
                .uri("/api/franchises/{idFranchise}/branches", idFranchise)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchFullResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void addProductToBranch_endpoint() {
        String idBranch = "BR001";

        AddProductToBranchRequestDto request = new AddProductToBranchRequestDto("PR001", 50);
        BranchModel branchModel = new BranchModel("BR001", "Sucursal Centro",null, null);
        List<ProductResponseDto> products = List.of(new ProductResponseDto("PR001", "Laptop", 50));
        BranchFullResponseDto responseDto = new BranchFullResponseDto("BR001", "Sucursal Centro", products);

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(service.addProductToBranch(idBranch, request.getProductId(), request.getStock())).willReturn(Mono.just(branchModel));
        given(mapper.toDtoFullBranch(branchModel)).willReturn(responseDto);

        client.post()
                .uri("/api/branches/{idBranch}/products", idBranch)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BranchFullResponseDto.class)
                .isEqualTo(responseDto);
    }


    @Test
    void createProduct_endpoint() {
        String idFranchise = "FR001";
        ProductRequestDto request = new ProductRequestDto("Laptop Dell", 50);
        ProductModel model = new ProductModel("PR001", "Laptop Dell", 50, idFranchise);
        ProductResponseDto response = new ProductResponseDto("PR001", "Laptop Dell", 50);

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(mapper.toModelProduct(any())).willReturn(model);
        given(service.createProduct(model, idFranchise)).willReturn(Mono.just(model));
        given(mapper.toDtoProduct(any())).willReturn(response);

        client.post().uri("/api/franchises/{idFranchise}/products", idFranchise)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void getFranchiseById_endpoint() {
        String idFranchise = "FR001";

        FranchiseModel model = new FranchiseModel("FR001", "Alkomprar", null);
        FranchiseFullResponseDto response = new FranchiseFullResponseDto("FR001", "Alkomprar", null);

        given(service.findFranchiseById(idFranchise)).willReturn(Mono.just(model));
        given(mapper.toDtoFullFranchise(model)).willReturn(response);

        client.get()
                .uri("/api/franchises/{idFranchise}", idFranchise)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseFullResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void getBranchById_endpoint() {
        String idBranch = "BR001";

        BranchModel model = new BranchModel("BR001", "Sucursal Centro", null, null);
        BranchFullResponseDto response = new BranchFullResponseDto("BR001", "Sucursal Centro", List.of());

        given(service.findBranchById(idBranch)).willReturn(Mono.just(model));
        given(mapper.toDtoFullBranch(model)).willReturn(response);

        client.get()
                .uri("/api/branches/{idBranch}", idBranch)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BranchFullResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void getProductByFranchiseId_endpoint() {
        String idFranchise = "FR001";

        ProductModel product1 = new ProductModel("PR001", "Laptop", 50, idFranchise);
        ProductModel product2 = new ProductModel("PR002", "Mouse", 100, idFranchise);
        List<ProductModel> productList = List.of(product1, product2);

        List<ProductResponseDto> response =
                List.of(
                        new ProductResponseDto("PR001", "Laptop", 50),
                        new ProductResponseDto("PR002", "Mouse", 100)
                );

        given(service.findProductsByFranchiseId(idFranchise)).willReturn(Flux.fromIterable(productList));
        given(mapper.toDtoProductList(productList)).willReturn(response);

        client.get()
                .uri("/api/franchises/{idFranchise}/products", idFranchise)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void updateNameFranchise_endpoint() {
        String idFranchise = "FR001";
        FranchiseRequestDto request = new FranchiseRequestDto("Alkomprar Renovado");
        FranchiseModel model = new FranchiseModel("FR001", "Alkomprar Renovado", null);
        FranchiseResponseDto response = new FranchiseResponseDto("FR001", "Alkomprar Renovado");

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(service.updateNameFranchise(idFranchise, request.getName())).willReturn(Mono.just(model));
        given(mapper.toDtoFranchise(any())).willReturn(response);

        client.patch()
                .uri("/api/franchises/{idFranchise}/name", idFranchise)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponseDto.class)
                .isEqualTo(response);
    }
    @Test
    void updateNameBranch_endpoint() {
        String idBranch = "BR001";
        BranchRequestDto request = new BranchRequestDto("Sucursal Centro Renovada");
        BranchModel model = new BranchModel("BR001", "Sucursal Centro Renovada", null, null);
        BranchResponseDto response = new BranchResponseDto("BR001", "Sucursal Centro Renovada");

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(service.updateNameBranch(idBranch, request.getName())).willReturn(Mono.just(model));
        given(mapper.toDtoBranch(any())).willReturn(response);

        client.patch()
                .uri("/api/branches/{idBranch}/name", idBranch)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BranchResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void updateNameProduct_endpoint() {
        String idProduct = "PR001";
        NameProductRequestDto request = new NameProductRequestDto("Laptop Dell XPS");
        ProductModel model = new ProductModel("PR001", "Laptop Dell XPS", 50, "1F");
        ProductNameResponseDto responseDto = new ProductNameResponseDto("PR001", "Laptop Dell XPS");

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(service.updateNameProduct(idProduct, request.getName())).willReturn(Mono.just(model));
        given(mapper.toDtoProductName(any())).willReturn(responseDto);

        client.patch()
                .uri("/api/products/{idProduct}/name", idProduct)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductNameResponseDto.class)
                .isEqualTo(responseDto);
    }

    @Test
    void updateStockProduct_endpoint() {
        String idProduct = "PR001";
        StockProductRequestDto request = new StockProductRequestDto(75);
        ProductModel model = new ProductModel("PR001", "Laptop Dell", 75, "1F");
        ProductResponseDto response = new ProductResponseDto("PR001", "Laptop Dell", 75);

        given(validator.validate(any())).willReturn(Mono.just(request));
        given(service.updateStockProduct(idProduct, request.getStock())).willReturn(Mono.just(model));
        given(mapper.toDtoProduct(any())).willReturn(response);

        client.patch()
                .uri("/api/products/{idProduct}/stock", idProduct)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponseDto.class)
                .isEqualTo(response);
    }

    @Test
    void deleteProductOfBranch_endpoint() {
        String idBranch = "BR001";
        String idProduct = "PR001";

        given(service.deleteProductOfBranch(idBranch, idProduct)).willReturn(Mono.empty());

        client.delete()
                .uri("/api/branches/{idBranch}/products/{idProduct}", idBranch, idProduct)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    void getProductLargestStock_endpoint() {
        String idFranchise = "FR001";

        ProductLargestStockByBranch result = new ProductLargestStockByBranch(
                "1B", "Laptop Dell", "BR001", "Sucursal Centro", 150
        );

        when(service.findProductLargestStock(idFranchise))
                .thenReturn(Flux.just(result));

        client.get()
                .uri("/api/franchises/{idFranchise}/largest-stock", idFranchise)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductLargestStockByBranch.class)
                .contains(result);
    }
}
