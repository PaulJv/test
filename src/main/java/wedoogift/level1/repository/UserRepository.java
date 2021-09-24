package wedoogift.level1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wedoogift.level1.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
