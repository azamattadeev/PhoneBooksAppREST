package phonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.entities.User;
import phonebooks.repositories.PhoneBookEntryRepository;

import java.util.ArrayList;

@Service
@Transactional
public class PhoneBookEntryService {
    private PhoneBookEntryRepository repository;
    private UserService userService;

    @Autowired
    public PhoneBookEntryService (PhoneBookEntryRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public PhoneBookEntry create(Long ownerId, PhoneBookEntry entry) {
        PhoneBookEntry saved = repository.save(entry);
        User user = userService.getById(ownerId);
        if (user.getContacts() == null) {
            user.setContacts(new ArrayList<>());
        }
        user.getContacts().add(entry);
        return saved;
    }

    public PhoneBookEntry findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PhoneBookEntry update(PhoneBookEntry phoneBookEntry) {
        return repository.save(phoneBookEntry);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
