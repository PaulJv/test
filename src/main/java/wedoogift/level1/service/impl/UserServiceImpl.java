package wedoogift.level1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wedoogift.level1.common.error.UserNotFoundException;
import wedoogift.level1.model.Distribution;
import wedoogift.level1.model.User;
import wedoogift.level1.model.Wallet;
import wedoogift.level1.repository.UserRepository;
import wedoogift.level1.service.IUserService;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final Clock clock;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this(userRepository, Clock.systemDefaultZone());
    }

    public UserServiceImpl(UserRepository userRepository, Clock clock){
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public double calculateBalance(int userId, String voucherType) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Distribution> activeDistribution = user.getDistributions().stream()
                .filter(d -> {
                    boolean condDate = LocalDate.now(this.clock).isAfter(d.getStartDate().minusDays(1)) && LocalDate.now(this.clock).isBefore(d.getEndDate());
                    boolean condType = d.getVoucherType().equals(voucherType);
                    return condDate && condType;
                })
                .collect(Collectors.toList());
        Wallet targetWallet =  user.getWallet().stream()
                .filter(w -> w.getWalletType().equals(voucherType))
                .findFirst()
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setAmount(0);
                    w.setWalletType(voucherType);
                    user.getWallet().add(w);
                    w.setUser(user);
                    return w;
                });
        double amount = activeDistribution.stream().mapToDouble(Distribution::getAmount).sum() + targetWallet.getAmount();
        if(amount > 0) {
            targetWallet.setAmount(amount);
            this.userRepository.save(user);
        }
        return amount;
    }
}
