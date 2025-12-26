package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.franchise.FranchiseRequestDto;
import co.com.bancolombia.api.dto.response.franchise.FranchiseFullResponseDto;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branches", ignore = true)
    FranchiseModel toModelFranchise(FranchiseRequestDto franchiseRequestDto);
    FranchiseFullResponseDto toDtoFullFranchise(FranchiseModel franchiseModel);
}
