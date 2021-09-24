package wedoogift.level1.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DistributeBodyDTO {
    @NotNull(message = "UserId is mandatory")
    @Min(value = 1, message = "UserId cannot be less than 1")
    private int userId;
    @NotNull
    @NotEmpty(message = "Amount list cannot be empty")
    private List<@DecimalMin(value = "0.0", message = "Cannot be less than 0") Double> amounts;
}
