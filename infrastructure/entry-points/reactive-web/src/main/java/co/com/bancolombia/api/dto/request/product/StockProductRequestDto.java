package co.com.bancolombia.api.dto.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockProductRequestDto implements Serializable {
    @NotNull(message = "The stock must no be null")
    @Min(value = 1, message = "The stock must be greater 1")
    private Integer stock;
}
