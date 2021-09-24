package wedoogift.level1.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {
    private int code;
    private String message;
}
