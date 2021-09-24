package wedoogift.level1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wedoogift.level1.common.enums.VoucherTypeEnum;
import wedoogift.level1.model.Company;
import wedoogift.level1.model.Distribution;
import wedoogift.level1.model.User;
import wedoogift.level1.model.Wallet;
import wedoogift.level1.model.dto.CompanyDTO;
import wedoogift.level1.model.dto.DistributionDTO;
import wedoogift.level1.model.dto.OutputDTO;
import wedoogift.level1.model.dto.UserDTO;
import wedoogift.level1.model.dto.WalletDTO;
import wedoogift.level1.repository.CompanyRepository;
import wedoogift.level1.repository.DistributionRepository;
import wedoogift.level1.repository.UserRepository;
import wedoogift.level1.service.IDistributionService;
import wedoogift.level1.service.IUserService;
import wedoogift.level1.service.impl.DistributionServiceImpl;
import wedoogift.level1.service.impl.UserServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Level1Application.class)
class Level1ApplicationTests {

    private static final int GIFT_WALLET_ID = 1;
    private static final int FOOD_WALLET_ID = 2;

    private IDistributionService distributionService;

    private IUserService userService;

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void init() {
        this.distributionService = new DistributionServiceImpl(userRepository, companyRepository, distributionRepository);
        this.userService = new UserServiceImpl(userRepository);
    }

    @Test
    void it() {
        generateDatabase();

        this.distributionService.distributeGiftCards(1, 1, List.of(50.0));
        this.distributionService.distributeMealVouchers(1, 1, List.of(250.0));
        this.distributionService.distributeGiftCards(2, 1, List.of(100.0));
        this.distributionService.distributeGiftCards(3, 2, List.of(1000.0));

        Optional<User> user1 = this.userRepository.findById(1);
        assertTrue(user1.isPresent());
        double user1BalanceGift = this.userService.calculateBalance(1, "GIFT");
        assertEquals(150, user1BalanceGift);
        double user1BalanceFood = this.userService.calculateBalance(1, "FOOD");
        assertEquals(250, user1BalanceFood);

        Optional<User> user2 = this.userRepository.findById(2);
        assertTrue(user2.isPresent());
        double user2BalanceGift = this.userService.calculateBalance(2, "GIFT");
        assertEquals(100, user2BalanceGift);
        double user2BalanceFood = this.userService.calculateBalance(2, "FOOD");
        assertEquals(0, user2BalanceFood);

        Optional<User> user3 = this.userRepository.findById(2);
        assertTrue(user3.isPresent());
        double user3BalanceGift = this.userService.calculateBalance(3, "GIFT");
        assertEquals(1000, user3BalanceGift);
        double user3BalanceFood = this.userService.calculateBalance(3, "FOOD");
        assertEquals(0, user3BalanceFood);

        Optional<Company> company1 = this.companyRepository.findById(1);
        assertTrue(company1.isPresent());
        assertEquals("Wedoogift", company1.get().getName());
        assertEquals(600, company1.get().getBalance());

        Optional<Company> company2 = this.companyRepository.findById(2);
        assertTrue(company2.isPresent());
        assertEquals("Wedoofood", company2.get().getName());
        assertEquals(2000, company2.get().getBalance());

        try {
            //Update user balance
            jsonDump();
        } catch (IOException io){
            System.err.println("Json write error " + io.getMessage());
        }
    }

    private void jsonDump() throws IOException {
        OutputDTO output = new OutputDTO();

        List<User> users = this.userRepository.findAll();
        List<UserDTO> userDTOs = users.stream().map(u -> {
            UserDTO currentUser = new UserDTO();
            currentUser.setId(u.getId());
            List<WalletDTO> userWallets = u.getWallet().stream().map(w -> {
                WalletDTO currentWallet = new WalletDTO();
                if(w.getWalletType().equals(VoucherTypeEnum.GIFT.getCode())){
                    currentWallet.setId(GIFT_WALLET_ID);
                    currentWallet.setAmount(w.getAmount());
                }
                if(w.getWalletType().equals(VoucherTypeEnum.FOOD.getCode())){
                    currentWallet.setId(FOOD_WALLET_ID);
                    currentWallet.setAmount(w.getAmount());
                }
                return currentWallet;
            }).collect(Collectors.toList());
            currentUser.setBalance(userWallets);
            return currentUser;
        }).collect(Collectors.toList());
        output.setUsers(userDTOs);

        List<Company> companies = this.companyRepository.findAll();
        List<CompanyDTO> companyDTOS = companies.stream().map(c -> {
            CompanyDTO result = new CompanyDTO();
            result.setId(c.getId());
            result.setName(c.getName());
            result.setBalance(c.getBalance());
            return result;
        }).collect(Collectors.toList());
        output.setCompanies(companyDTOS);

        List<Distribution> distributions = this.distributionRepository.findAll();
        List<DistributionDTO> distributionDTOS = distributions.stream().map(d -> {
            DistributionDTO result = new DistributionDTO();
            result.setId(d.getId());
            result.setUserId(d.getUser().getId());
            result.setCompanyId(d.getCompany().getId());
            result.setAmount(d.getAmount());
            result.setStartDate(d.getStartDate());
            result.setEndDate(d.getEndDate());
            if(d.getVoucherType().equals(VoucherTypeEnum.GIFT.getCode())){
                result.setWalletId(GIFT_WALLET_ID);
            }
            if(d.getVoucherType().equals(VoucherTypeEnum.FOOD.getCode())){
                result.setWalletId(FOOD_WALLET_ID);
            }
            return result;
        }).collect(Collectors.toList());
        output.setDistributions(distributionDTOS);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("output.json"), output);
    }

    private void generateDatabase() {
        User user1 = new User();
        user1.setDistributions(new ArrayList<>());
        Wallet walletUser1 = new Wallet();
        walletUser1.setUser(user1);
        walletUser1.setWalletType("GIFT");
        walletUser1.setAmount(100);
        user1.setWallet(List.of(walletUser1));
        User user2 = new User();
        user2.setDistributions(new ArrayList<>());
        User user3 = new User();
        user3.setDistributions(new ArrayList<>());

        Company company1 = new Company();
        company1.setBalance(1000);
        company1.setName("Wedoogift");
        Company company2 = new Company();
        company2.setBalance(3000);
        company2.setName("Wedoofood");

        this.userRepository.saveAll(Arrays.asList(user1, user2, user3));
        this.companyRepository.saveAll(Arrays.asList(company1, company2));
    }

}
