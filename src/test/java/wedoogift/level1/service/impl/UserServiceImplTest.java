package wedoogift.level1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import wedoogift.level1.common.error.UserNotFoundException;
import wedoogift.level1.model.Distribution;
import wedoogift.level1.model.User;
import wedoogift.level1.repository.UserRepository;
import wedoogift.level1.service.IUserService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
@PrepareForTest({LocalDate.class, UserServiceImpl.class})
@RunWith(PowerMockRunner.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private Clock clock;

    private IUserService userService;

    @BeforeEach
    public void init(){
        this.clock = Clock.systemDefaultZone();
        this.userService = new UserServiceImpl(userRepository, this.clock);
    }

    @Test
    public void calculateBalance_shouldThrowUserNotFoundException(){
        Mockito.when(userRepository.findById(Mockito.eq(42))).thenReturn(Optional.empty());

        try {
            this.userService.calculateBalance(42, "GIFT");
            fail("Should throw UserNotFoundException");
        } catch (UserNotFoundException unfe) {
            assertEquals("User 42 not found", unfe.getMessage());
        }
    }

    @Test
    public void calculateBalanceShouldReturn0Amount_IfEveryVoucherIsExpired(){
        User mockUser = new User();
        mockUser.setId(42);
        mockUser.setWallet(new ArrayList<>());
        mockUser.setDistributions(generateDistributionList());
        LocalDate now = LocalDate.of(2022,1,1);
        // create a fixed clock (it always return the same date, using the system's default timezone)
        ZoneId zone = ZoneId.systemDefault();
        this.clock = Clock.fixed(now.atStartOfDay(zone).toInstant(), zone);

        this.userService = new UserServiceImpl(userRepository, this.clock);

        PowerMockito.stub(PowerMockito.method(LocalDate.class,"now")).toReturn(now);
        Mockito.when(this.userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockUser));

        double result = this.userService.calculateBalance(42, "GIFT");

        assertEquals(0, result);
    }

    @Test
    public void calculateBalanceShouldReturnTheProperAmount_IfSomeVouchersAreExpired(){
        User mockUser = new User();
        mockUser.setId(42);
        mockUser.setWallet(new ArrayList<>());
        mockUser.setDistributions(generateDistributionList());
        LocalDate now = LocalDate.of(2021, 7, 17);
        ZoneId zone = ZoneId.systemDefault();
        this.clock = Clock.fixed(now.atStartOfDay(zone).toInstant(), zone);

        this.userService = new UserServiceImpl(userRepository, this.clock);

        Mockito.when(this.userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockUser));

        double result = this.userService.calculateBalance(42, "GIFT");

        assertEquals(150, result);
    }

    @Test
    public void calculateBalanceShouldReturnTheProperAmount_IfNoVouchersAreExpired(){
        User mockUser = new User();
        mockUser.setId(42);
        mockUser.setWallet(new ArrayList<>());
        mockUser.setDistributions(generateDistributionList());
        LocalDate now = LocalDate.of(2020, 12, 31);
        ZoneId zone = ZoneId.systemDefault();
        this.clock = Clock.fixed(now.atStartOfDay(zone).toInstant(), zone);

        this.userService = new UserServiceImpl(userRepository, this.clock);

        Mockito.when(this.userRepository.findById(Mockito.eq(42))).thenReturn(Optional.of(mockUser));

        double result = this.userService.calculateBalance(42, "GIFT");

        assertEquals(1150, result);
    }

    private List<Distribution> generateDistributionList(){
        List<Distribution> distributions = new ArrayList<>();
        distributions.add(generateDistribution(1, 50, LocalDate.of(2020,9,16), LocalDate.of(2021,9,15)));
        distributions.add(generateDistribution(2, 100, LocalDate.of(2020,8, 1), LocalDate.of(2021, 7,31)));
        distributions.add(generateDistribution(3, 1000, LocalDate.of(2020, 5, 1), LocalDate.of(2021, 4, 30)));
        return distributions;
    }

    private Distribution generateDistribution(int id, double amount, LocalDate startDate, LocalDate endDate){
        Distribution distribution = new Distribution();
        distribution.setId(id);
        distribution.setStartDate(startDate);
        distribution.setEndDate(endDate);
        distribution.setAmount(amount);
        distribution.setVoucherType("GIFT");
        return distribution;
    }

}
