package phonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.stores.UserStorage;

import java.util.List;

@Service
public class PhoneBookEntryService {
    private UserStorage userStorage;

    @Autowired
    public PhoneBookEntryService (UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public PhoneBookEntry create(Long ownerId, PhoneBookEntry entry) {
        return userStorage.savePhoneBookEntryForUser(ownerId, entry);
    }

    public PhoneBookEntry getById(Long id) {
        return userStorage.getPhoneBookEntryById(id);
    }

    public PhoneBookEntry update(Long id, PhoneBookEntry phoneBookEntry) {
        return userStorage.updatePhoneBookEntryById(id, phoneBookEntry);
    }

    public boolean deleteById(Long id) {
        return userStorage.deletePhoneBookEntryById(id);
    }

    public List<PhoneBookEntry> getAll() {
        return userStorage.getAllPhoneBookEntries();
    }

    public List<PhoneBookEntry> getAllPhoneBookEntriesByUserId(Long id) {
        return userStorage.getAllPhoneBookEntriesByUserId(id);
    }

}
