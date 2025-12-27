package co.com.bancolombia.api.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductLargestStockResponseDto {
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer stock;
}
