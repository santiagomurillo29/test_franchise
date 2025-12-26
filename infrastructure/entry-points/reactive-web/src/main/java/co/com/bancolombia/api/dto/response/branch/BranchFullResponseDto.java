package co.com.bancolombia.api.dto.response.branch;

import co.com.bancolombia.api.dto.response.product.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BranchFullResponseDto {
    private String id;
    private String name;
    private List<ProductResponseDto> products;
}
