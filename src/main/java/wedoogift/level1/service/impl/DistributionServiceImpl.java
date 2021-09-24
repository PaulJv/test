package wedoogift.level1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wedoogift.level1.common.enums.VoucherTypeEnum;
import wedoogift.level1.common.error.CompanyNotEnoughBalanceException;
import wedoogift.level1.common.error.CompanyNotFoundException;
import wedoogift.level1.common.error.UserNotFoundException;
import wedoogift.level1.model.Company;
import wedoogift.level1.model.Distribution;
import wedoogift.level1.model.User;
import wedoogift.level1.repository.CompanyRepository;
import wedoogift.level1.repository.DistributionRepository;
import wedoogift.level1.repository.UserRepository;
import wedoogift.level1.service.IDistributionService;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DistributionServiceImpl implements IDistributionService {

    private static final int MAX_AVAILABILITY_GIFT_CARDS = 365;
    private static final String FOOD_CODE = "FOOD";
    private static final String GIFT_CODE = "GIFT";

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final DistributionRepository distributionRepository;

    private final Clock clock;//For test

    @Autowired
    public DistributionServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, DistributionRepository distributionRepository){
            this(userRepository, companyRepository, distributionRepository, Clock.systemDefaultZone());
    }

    public DistributionServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, DistributionRepository distributionRepository, Clock clock){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.distributionRepository = distributionRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void distributeGiftCards(int userId, int companyId, List<Double> giftCards) {
        distributeVoucher(userId, companyId, giftCards, VoucherTypeEnum.getFromCode(GIFT_CODE));
    }

    @Override
    @Transactional
    public void distributeMealVouchers(int userId, int companyId, List<Double> mealVouchers) {
        distributeVoucher(userId, companyId, mealVouchers, VoucherTypeEnum.getFromCode(FOOD_CODE));
    }

    private void distributeVoucher(int userId, int companyId, List<Double> vouchers, VoucherTypeEnum voucherType){
        User user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
        double totalAmount = vouchers.stream().mapToDouble(Double::doubleValue).sum();

        List<Distribution> distribution = new ArrayList<>();
        vouchers.forEach(gc -> {
            if(totalAmount > company.getBalance()){
                throw new CompanyNotEnoughBalanceException(companyId, company.getBalance(), totalAmount);
            }
            Distribution current = new Distribution();
            current.setAmount(gc);
            current.setCompany(company);
            current.setUser(user);
            current.setStartDate(LocalDate.now());
            if(voucherType == VoucherTypeEnum.FOOD) {
                LocalDate endDateMonth = LocalDate.now(this.clock).plusYears(1).withMonth(2);
                current.setEndDate(endDateMonth.withDayOfMonth(endDateMonth.lengthOfMonth()));
            }
            if(voucherType == VoucherTypeEnum.GIFT) {
                current.setEndDate(LocalDate.now(this.clock).plusDays(MAX_AVAILABILITY_GIFT_CARDS));
            }
            current.setVoucherType(voucherType.getCode());
            company.setBalance(company.getBalance() - gc);
            distribution.add(current);
        });

        this.distributionRepository.saveAll(distribution);
        this.companyRepository.save(company);
    }
}
