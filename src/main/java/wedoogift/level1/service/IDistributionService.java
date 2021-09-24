package wedoogift.level1.service;

import java.util.List;

public interface IDistributionService {
    void distributeGiftCards(int userId, int companyId, List<Double> giftCards);

    void distributeMealVouchers(int userId, int companyId, List<Double> mealVouchers);
}
