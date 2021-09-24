package wedoogift.level1.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wedoogift.level1.common.enums.VoucherTypeEnum;
import wedoogift.level1.model.dto.BalanceResponseDTO;
import wedoogift.level1.model.dto.WalletDTO;
import wedoogift.level1.service.IUserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private static final int GIFT_WALLET_ID = 1;
    private static final int FOOD_WALLET_ID = 2;

    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @RequestMapping("/user/{id}/balance")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BalanceResponseDTO getBalance(@PathVariable int id){
        double balanceVoucherMeal = this.userService.calculateBalance(id, VoucherTypeEnum.FOOD.getCode());
        double balanceGiftCards = this.userService.calculateBalance(id, VoucherTypeEnum.GIFT.getCode());

        BalanceResponseDTO responseDTO = new BalanceResponseDTO();
        responseDTO.setUserId(id);
        List<WalletDTO> wallets = new ArrayList<>();
        WalletDTO voucherMealWallet = new WalletDTO();
        voucherMealWallet.setId(FOOD_WALLET_ID);
        voucherMealWallet.setAmount(balanceVoucherMeal);
        WalletDTO giftCardsWallet = new WalletDTO();
        giftCardsWallet.setId(GIFT_WALLET_ID);
        giftCardsWallet.setAmount(balanceGiftCards);
        wallets.add(giftCardsWallet);
        wallets.add(voucherMealWallet);
        responseDTO.setWallets(wallets);

        return responseDTO;
    }
}
