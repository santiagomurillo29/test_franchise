package co.com.bancolombia.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchProductTuple {
    private String id;
    private String name;
    private Integer stock;
}
