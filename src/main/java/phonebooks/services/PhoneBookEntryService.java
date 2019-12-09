package phonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.entities.User;
import phonebooks.repositories.PhoneBookEntryRepository;

import java.util.ArrayList;
import java.util.List;

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
        if (user != null) {
            if (user.getContacts() == null) {
                user.setContacts(new ArrayList<>());
            }
            user.getContacts().add(entry);
            return saved;
        } else return null;
    }

    public PhoneBookEntry getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PhoneBookEntry update(Long id, PhoneBookEntry phoneBookEntry) {
        if (repository.existsById(id)) {
            phoneBookEntry.setId(id);
            return repository.save(phoneBookEntry);
        } else {
            return null;
        }
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<PhoneBookEntry> getAll() {
        return repository.findAll();
    }

}
