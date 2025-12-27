package co.com.bancolombia.api.dto.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToBranchRequestDto {
    @NotBlank(message = "Product id must not be null")
    private String productId;

    @NotNull(message = "Stock must not be null")
    @Min(value = 1, message = "Stock must be greater than 0")
    private Integer stock;
}
