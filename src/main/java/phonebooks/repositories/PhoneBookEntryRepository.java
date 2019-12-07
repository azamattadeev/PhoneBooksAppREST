package phonebooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import phonebooks.entities.PhoneBookEntry;

public interface PhoneBookEntryRepository extends JpaRepository<PhoneBookEntry, Long> {
}
