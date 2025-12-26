package co.com.bancolombia.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchEntity {
    @Id
    private String id;
    private String name;
    private String franchiseId;
    private List<BranchProductTuple> products;
}
