package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.request.franchise.FranchiseRequestDto;
import co.com.bancolombia.api.dto.request.validation.RequestValidator;
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
}
