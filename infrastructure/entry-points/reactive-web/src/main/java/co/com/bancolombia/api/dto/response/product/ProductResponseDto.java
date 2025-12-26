package co.com.bancolombia.api.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponseDto {
    private String id;
    private String name;
    private Integer stock;
}
