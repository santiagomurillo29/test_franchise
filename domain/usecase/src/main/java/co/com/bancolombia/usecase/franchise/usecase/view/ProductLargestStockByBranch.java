package co.com.bancolombia.usecase.franchise.usecase.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLargestStockByBranch {
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer stock;
}
