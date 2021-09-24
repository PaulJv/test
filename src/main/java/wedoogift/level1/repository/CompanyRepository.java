package wedoogift.level1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wedoogift.level1.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
