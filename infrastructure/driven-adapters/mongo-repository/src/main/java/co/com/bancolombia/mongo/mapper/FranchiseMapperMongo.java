package co.com.bancolombia.mongo.mapper;

import co.com.bancolombia.model.franchise.model.BranchModel;
import co.com.bancolombia.model.franchise.model.FranchiseModel;
import co.com.bancolombia.mongo.entity.BranchEntity;
import co.com.bancolombia.mongo.entity.FranchiseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FranchiseMapperMongo {

    FranchiseEntity toEntityFranchise(FranchiseModel franchiseModel);
    @Mapping(target = "branches", ignore = true)
    FranchiseModel toModelFranchise(FranchiseEntity franchiseEntity);

    @Mapping(target = "franchiseId", ignore = true)
    BranchEntity toEntityBranch(BranchModel branchModel);
    @Mapping(target = "products", source = "products")
    BranchModel toModelBranch(BranchEntity branchEntity);

    List<BranchModel> toModelBranchList(List<BranchEntity> branchEntities);
}