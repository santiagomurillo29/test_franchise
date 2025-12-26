package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.branch.BranchRequestDto;
import co.com.bancolombia.api.dto.request.franchise.FranchiseRequestDto;
import co.com.bancolombia.api.dto.request.product.ProductRequestDto;
import co.com.bancolombia.api.dto.response.branch.BranchFullResponseDto;
import co.com.bancolombia.api.dto.response.franchise.FranchiseFullResponseDto;
import co.com.bancolombia.api.dto.response.product.ProductResponseDto;
import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.model.franchise.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branches", ignore = true)
    FranchiseModel toModelFranchise(FranchiseRequestDto franchiseRequestDto);
    FranchiseFullResponseDto toDtoFullFranchise(FranchiseModel franchiseModel);

    @Mapping(target = "franchiseId", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "id", ignore = true)
    BranchModel toModelBranch(BranchRequestDto branchRequestDto);
    BranchFullResponseDto toDtoFullBranch(BranchModel branchModel);

    @Mapping(target = "franchiseId", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductModel toModelProduct(ProductRequestDto productRequestDto);
    ProductResponseDto toDtoProduct(ProductModel productModel);
}
