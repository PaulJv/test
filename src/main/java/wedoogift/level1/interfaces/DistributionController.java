package wedoogift.level1.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wedoogift.level1.model.dto.DistributeBodyDTO;
import wedoogift.level1.service.IDistributionService;

import javax.validation.Valid;

@RestController
public class DistributionController {

    private final IDistributionService distributionService;

    public DistributionController(IDistributionService distributionService){
        this.distributionService = distributionService;
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping
    @RequestMapping("/company/{id}/giftCards/distribute")
    public void distributeGiftCards(@PathVariable int id, @RequestBody @Valid DistributeBodyDTO distributeBody){
        this.distributionService.distributeGiftCards(distributeBody.getUserId(), id, distributeBody.getAmounts());
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping
    @RequestMapping("/company/{id}/mealVouchers/distribute")
    public void distributeMealVouchers(@PathVariable int id, @RequestBody @Valid DistributeBodyDTO distributeBody){
        this.distributionService.distributeMealVouchers(distributeBody.getUserId(), id, distributeBody.getAmounts());
    }
}
