package wedoogift.level1.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BalanceResponseDTO {
    private int userId;
    private List<WalletDTO> wallets;
}
