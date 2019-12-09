package phonebooks.stores;

import org.springframework.stereotype.Component;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserStorage {
    private ConcurrentHashMap<Long, User> users;

    private AtomicLong nextUserId;
    private AtomicLong nextPhoneBookEntryId;

    public UserStorage() {
        users = new ConcurrentHashMap<>();
        nextUserId = new AtomicLong(1L);
        nextPhoneBookEntryId = new AtomicLong(1L);
    }

    public User saveUser(User user) {
        if (user != null) {
            user.setId(getNextUserId());
            users.put(user.getId(), user);
            return user;
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public boolean existsUserById(Long id) {
        return users.containsKey(id);
    }

    public User updateUser(User user) {
        User oldUser = users.get(user.getId());
        user.setContacts(oldUser.getContacts());
        users.replace(user.getId(), user);
        return user;
    }

    public void deleteUserById(Long id) {
        users.remove(id);
    }

    public PhoneBookEntry savePhoneBookEntryForUser(Long ownerId, PhoneBookEntry entry) {
        if (existsUserById(ownerId)) {
            entry.setId(getNextPhoneBookEntryId());
            getUserById(ownerId).getContacts().put(entry.getId(), entry);
            return entry;
        } else {
            return null;
        }
    }

    public PhoneBookEntry getPhoneBookEntryById(Long id) {
        PhoneBookEntry entry = null;
        for (User user : users.values()) {
            if (user.getContacts().containsKey(id)) {
                entry = user.getContacts().get(id);
            }
        }
        return entry;
    }

    public List<PhoneBookEntry> getAllPhoneBookEntries() {
        List<PhoneBookEntry> entries = new ArrayList<>();
        for(User user : users.values()) {
            entries.addAll(user.getContacts().values());
        }
        return entries;
    }

    public PhoneBookEntry updatePhoneBookEntryById(Long id, PhoneBookEntry entry) {
        PhoneBookEntry oldEntry = getPhoneBookEntryById(id);
        if (oldEntry != null) {
            oldEntry.setPhoneNumber(entry.getPhoneNumber());
            oldEntry.setContactName(entry.getContactName());
            return oldEntry;
        } else {
            return null;
        }
    }

    public boolean deletePhoneBookEntryById(Long id) {
        for (User user : users.values()) {
            if (user.getContacts().containsKey(id)) {
               user.getContacts().remove(id);
               return true;
            }
        }
        return false;
    }

    public List<PhoneBookEntry> getAllPhoneBookEntriesByUserId(Long id) {
        if (users.containsKey(id)) {
            return new ArrayList<>(users.get(id).getContacts().values());
        } else {
            return null;
        }
    }

    public List<PhoneBookEntry> getAllPhoneBookEntriesByPhoneNumber(String phoneNumber) {
        List<PhoneBookEntry> entries = new ArrayList<>();

        for (User user : users.values()) {
            for (PhoneBookEntry entry : user.getContacts().values()) {
                if (entry.getPhoneNumber().equals(phoneNumber)) {
                    entries.add(entry);
                }
            }
        }

        return entries;
    }

    private Long getNextUserId() {
        return nextUserId.getAndIncrement();
    }

    private Long getNextPhoneBookEntryId() {
        return nextPhoneBookEntryId.getAndIncrement();
    }

}
