package co.com.bancolombia.api.dto.response.franchise;

import co.com.bancolombia.api.dto.response.branch.BranchFullResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FranchiseFullResponseDto {
    private String id;
    private String name;
    private List<BranchFullResponseDto> branches;
}
