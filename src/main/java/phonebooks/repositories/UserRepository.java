package phonebooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import phonebooks.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
