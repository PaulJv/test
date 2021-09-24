package wedoogift.level1.common.error;

public class CompanyNotEnoughBalanceException extends RuntimeException{
    public CompanyNotEnoughBalanceException(int companyId, double balance, double request){
        super("Company " + companyId + " has not enough balance. requested: " + request + ", left: " + balance);
    }
}
