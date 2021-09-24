package wedoogift.level1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
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

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({LocalDate.class, DistributionServiceImpl.class})
@RunWith(PowerMockRunner.class)
class DistributionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private DistributionRepository distributionRepository;

    @Captor
    private ArgumentCaptor<List<Distribution>> distributionListCaptor;

    @Captor
    private ArgumentCaptor<Company> companyCaptor;

    private Clock clock;

    private IDistributionService distributionService;

    @BeforeEach
    public void init(){
        LocalDate now = LocalDate.of(2021, 9, 13);
        ZoneId zone = ZoneId.systemDefault();
        Clock clockNow = Clock.fixed(now.atStartOfDay(zone).toInstant(), zone);
        this.distributionService = new DistributionServiceImpl(userRepository, companyRepository, distributionRepository, clockNow);
    }

    @Test
    public void distributeMealVoucherShouldThrownCompanyNotFound(){
        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(new User()));
        Mockito.when(companyRepository.findById(Mockito.eq(42))).thenReturn(Optional.empty());

        try {
            this.distributionService.distributeMealVouchers(42, 42, new ArrayList<>());
            fail("Should throw CompanyNotFoundException");
        } catch (CompanyNotFoundException cnfe) {
            assertEquals("Company 42 not found", cnfe.getMessage());
        }
    }

    @Test
    public void distributeMealVoucherShouldThrownUserNotFound(){
        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.empty());

        try {
            this.distributionService.distributeMealVouchers(42, 42, new ArrayList<>());
            fail("Should throw UserNotFoundException");
        } catch (UserNotFoundException unfe) {
            assertEquals("User 42 not found", unfe.getMessage());
        }
    }

    @Test
    public void distributeMealVoucherShouldThrownCompanyNotEnoughBalanceException_IfCompanyHasLessThanTotalAmount(){
        Company mockCompany = new Company();
        mockCompany.setBalance(1000);
        List<Double> mealVouchersInput = Arrays.asList(500.0,100.0,250.0,300.0);

        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(new User()));
        Mockito.when(companyRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockCompany));

        try {
            this.distributionService.distributeMealVouchers(42, 42, mealVouchersInput);
            fail("Should throw UserNotFoundException");
        } catch (CompanyNotEnoughBalanceException cnebe) {
            assertEquals("Company 42 has not enough balance. requested: 1150.0, left: 1000.0", cnebe.getMessage());
        }
    }

    @Test
    public void distributeMealVoucherShouldSaveDistributionAndUpdateCompanyBalance(){
        Company mockCompany = new Company();
        mockCompany.setId(42);
        mockCompany.setBalance(3000);
        User mockUser = new User();
        mockUser.setId(42);
        List<Double> mealVouchersInput = Arrays.asList(500.0,100.0,250.0,300.0);
        LocalDate now = LocalDate.of(2021, 9, 13);

        this.distributionService = new DistributionServiceImpl(userRepository, companyRepository, distributionRepository);

        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockUser));
        Mockito.when(companyRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockCompany));

        this.distributionService.distributeMealVouchers(42, 42, mealVouchersInput);

        Mockito.verify(this.distributionRepository).saveAll(this.distributionListCaptor.capture());
        Mockito.verify(this.companyRepository).save(this.companyCaptor.capture());

        assertEquals(1850.0, this.companyCaptor.getValue().getBalance());
        assertEquals(1150, this.distributionListCaptor.getValue().stream().mapToDouble(Distribution::getAmount).sum());
        assertEquals(4, this.distributionListCaptor.getValue().size());
        this.distributionListCaptor.getValue().forEach(d -> {
            assertEquals(42, d.getCompany().getId());
            assertEquals(42, d.getUser().getId());
            assertEquals(LocalDate.now(), d.getStartDate());
            assertEquals(LocalDate.now().plusYears(1).withMonth(2).withDayOfMonth(28), d.getEndDate());
            assertEquals(VoucherTypeEnum.FOOD.getCode(), d.getVoucherType());
        });

    }

    @Test
    public void distributeGiftCardShouldThrownCompanyNotFound(){
        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(new User()));
        Mockito.when(companyRepository.findById(Mockito.eq(42))).thenReturn(Optional.empty());

        try {
            this.distributionService.distributeGiftCards(42, 42, new ArrayList<>());
            fail("Should throw CompanyNotFoundException");
        } catch (CompanyNotFoundException cnfe) {
            assertEquals("Company 42 not found", cnfe.getMessage());
        }
    }

    @Test
    public void distributeGiftCardShouldThrownUserNotFound(){
        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.empty());

        try {
            this.distributionService.distributeGiftCards(42, 42, new ArrayList<>());
            fail("Should throw UserNotFoundException");
        } catch (UserNotFoundException unfe) {
            assertEquals("User 42 not found", unfe.getMessage());
        }
    }

    @Test
    public void distributeGiftCardShouldThrownCompanyNotEnoughBalanceException_IfCompanyHasLessThanTotalAmount(){
        Company mockCompany = new Company();
        mockCompany.setBalance(1000);
        List<Double> mealVouchersInput = Arrays.asList(500.0,100.0,250.0,300.0);

        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(new User()));
        Mockito.when(companyRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockCompany));

        try {
            this.distributionService.distributeGiftCards(42, 42, mealVouchersInput);
            fail("Should throw UserNotFoundException");
        } catch (CompanyNotEnoughBalanceException cnebe) {
            assertEquals("Company 42 has not enough balance. requested: 1150.0, left: 1000.0", cnebe.getMessage());
        }
    }

    @Test
    public void distributeGiftCardShouldSaveDistributionAndUpdateCompanyBalance(){
        Company mockCompany = new Company();
        mockCompany.setId(42);
        mockCompany.setBalance(3000);
        User mockUser = new User();
        mockUser.setId(42);
        List<Double> mealVouchersInput = Arrays.asList(500.0,100.0,250.0,300.0);

        this.distributionService = new DistributionServiceImpl(userRepository, companyRepository, distributionRepository);

        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockUser));
        Mockito.when(companyRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockCompany));

        this.distributionService.distributeGiftCards(42, 42, mealVouchersInput);

        Mockito.verify(this.distributionRepository).saveAll(this.distributionListCaptor.capture());
        Mockito.verify(this.companyRepository).save(this.companyCaptor.capture());

        assertEquals(1850.0, this.companyCaptor.getValue().getBalance());
        assertEquals(1150, this.distributionListCaptor.getValue().stream().mapToDouble(Distribution::getAmount).sum());
        assertEquals(4, this.distributionListCaptor.getValue().size());
        this.distributionListCaptor.getValue().forEach(d -> {
            assertEquals(42, d.getCompany().getId());
            assertEquals(42, d.getUser().getId());
            assertEquals(LocalDate.now(), d.getStartDate());
            assertEquals(LocalDate.now().plusYears(1), d.getEndDate());
            assertEquals(VoucherTypeEnum.GIFT.getCode(), d.getVoucherType());
        });

    }

}
