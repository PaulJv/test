package wedoogift.level1.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OutputDTO {
    private List<CompanyDTO> companies;
    private List<UserDTO> users;
    private List<DistributionDTO> distributions;
}
