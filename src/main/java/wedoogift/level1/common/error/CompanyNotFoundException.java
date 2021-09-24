package wedoogift.level1.common.error;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(int companyId){
        super("Company " + companyId + " not found");
    }
}
