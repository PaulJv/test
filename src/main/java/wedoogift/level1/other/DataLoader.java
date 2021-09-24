package wedoogift.level1.other;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wedoogift.level1.model.Company;
import wedoogift.level1.model.User;
import wedoogift.level1.model.Wallet;
import wedoogift.level1.repository.CompanyRepository;
import wedoogift.level1.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class DataLoader{

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public DataLoader(UserRepository userRepository, CompanyRepository companyRepository){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @RequestMapping("/database/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generateDatabase() {
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
