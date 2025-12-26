package co.com.bancolombia.mongo.adapter;

import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.repository.MongoDBRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FranchiseAdapterMongo extends AdapterOperations<Object/* change for domain model */, Object/* change for adapter model */, String, MongoDBRepository>
// implements ModelRepository from domain
{

    public FranchiseAdapterMongo(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Object.class/* change for domain model */));
    }
}
