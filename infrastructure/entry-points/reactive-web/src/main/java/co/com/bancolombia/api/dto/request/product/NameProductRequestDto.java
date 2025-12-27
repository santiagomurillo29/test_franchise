package co.com.bancolombia.api.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameProductRequestDto implements Serializable {
    @NotBlank(message = "The name must not be empty and null")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "The name must not contain special characters")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;
}
