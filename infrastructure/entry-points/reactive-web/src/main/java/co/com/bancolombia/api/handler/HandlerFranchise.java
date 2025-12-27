package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.branch.BranchRequestDto;
import co.com.bancolombia.api.dto.request.franchise.FranchiseRequestDto;
import co.com.bancolombia.api.dto.request.product.AddProductToBranchRequestDto;
import co.com.bancolombia.api.dto.request.product.NameProductRequestDto;
import co.com.bancolombia.api.dto.request.product.ProductRequestDto;
import co.com.bancolombia.api.dto.request.product.StockProductRequestDto;
import co.com.bancolombia.api.dto.request.validation.RequestValidator;
import co.com.bancolombia.api.dto.response.product.ProductLargestStockResponseDto;
import co.com.bancolombia.api.mapper.FranchiseMapper;
import co.com.bancolombia.usecase.franchise.usecase.api.FranchiseServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerFranchise {

    private final FranchiseServicePort franchiseServicePort;
    private final FranchiseMapper franchiseMapper;
    private final RequestValidator validator;

    private static final String ID_FRANCHISE = "idFranchise";
    private static final String ID_BRANCH = "idBranch";
    private static final String ID_PRODUCT = "idProduct";

    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseRequestDto.class)
                .flatMap(validator::validate)
                .map(franchiseMapper::toModelFranchise)
                .flatMap(franchiseServicePort::createFranchise)
                .map(franchiseMapper::toDtoFullFranchise)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequestDto.class)
                .flatMap(validator::validate)
                .map(franchiseMapper::toModelBranch)
                .flatMap(model -> franchiseServicePort.createBranch(serverRequest.pathVariable(ID_FRANCHISE), model))
                .map(franchiseMapper::toDtoFullBranch)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductRequestDto.class)
                .flatMap(validator::validate)
                .map(franchiseMapper::toModelProduct)
                .flatMap(product -> franchiseServicePort.createProduct(product, serverRequest.pathVariable(ID_FRANCHISE)))
                .map(franchiseMapper::toDtoProduct)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> addProductToBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AddProductToBranchRequestDto.class)
                .flatMap(validator::validate)
                .flatMap(dto ->
                        franchiseServicePort.addProductToBranch(
                                serverRequest.pathVariable(ID_BRANCH),
                                dto.getProductId(),
                                dto.getStock()
                        )
                )
                .map(franchiseMapper::toDtoFullBranch)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> getProductLargestStock(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        franchiseServicePort.findProductLargestStock(
                                serverRequest.pathVariable(ID_FRANCHISE)
                        ),
                        ProductLargestStockResponseDto.class
                );
    }

    public Mono<ServerResponse> getFranchiseById(ServerRequest serverRequest) {
        return franchiseServicePort.findFranchiseById(serverRequest.pathVariable(ID_FRANCHISE))
                .map(franchiseMapper::toDtoFullFranchise)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> getBranchById(ServerRequest serverRequest) {
        return franchiseServicePort.findBranchById(serverRequest.pathVariable(ID_BRANCH))
                .map(franchiseMapper::toDtoFullBranch)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }


    public Mono<ServerResponse> getProductByFranchiseId(ServerRequest serverRequest) {
        return franchiseServicePort.findProductsByFranchiseId(serverRequest.pathVariable(ID_FRANCHISE))
                .collectList()
                .map(franchiseMapper::toDtoProductList)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StockProductRequestDto.class)
                .flatMap(validator::validate)
                .flatMap(dto -> franchiseServicePort.updateStockProduct(serverRequest.pathVariable(ID_PRODUCT), dto.getStock()))
                .map(franchiseMapper::toDtoProduct)
                .flatMap(updatedProduct ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updatedProduct)
                );
    }

    public Mono<ServerResponse> updateNameFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseRequestDto.class)
                .flatMap(validator::validate)
                .flatMap(franchiseDto -> franchiseServicePort.updateNameFranchise(serverRequest.pathVariable(ID_FRANCHISE), franchiseDto.getName()))
                .map(franchiseMapper::toDtoFranchise)
                .flatMap(updatedFranchise ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updatedFranchise)
                );
    }

    public Mono<ServerResponse> updateNameBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequestDto.class)
                .flatMap(validator::validate)
                .flatMap(branchDto -> franchiseServicePort.updateNameBranch(serverRequest.pathVariable(ID_BRANCH), branchDto.getName()))
                .map(franchiseMapper::toDtoBranch)
                .flatMap(updatedBranch ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updatedBranch)
                );
    }


    public Mono<ServerResponse> updateNameProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(NameProductRequestDto.class)
                .flatMap(validator::validate)
                .flatMap(productDto -> franchiseServicePort.updateNameProduct(serverRequest.pathVariable(ID_PRODUCT), productDto.getName()))
                .map(franchiseMapper::toDtoProductName)
                .flatMap(updatedProduct ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updatedProduct)
                );
    }

    public Mono<ServerResponse> deleteProductOfBranch(ServerRequest serverRequest) {
        return franchiseServicePort.deleteProductOfBranch(
                        serverRequest.pathVariable(ID_BRANCH),
                        serverRequest.pathVariable(ID_PRODUCT))
                .then(ServerResponse.noContent().build());
    }
}
