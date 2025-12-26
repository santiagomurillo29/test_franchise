package co.com.bancolombia.mongo.mapper;

import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.mongo.entity.FranchiseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FranchiseMapperMongo {

    FranchiseEntity toEntityFranchise(FranchiseModel franchiseModel);
    @Mapping(target = "branches", ignore = true)
    FranchiseModel toModelFranchise(FranchiseEntity franchiseEntity);
}