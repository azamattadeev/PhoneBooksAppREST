package phonebooks.servicestests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.entities.User;
import phonebooks.services.PhoneBookEntryService;
import phonebooks.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PhoneBookEntryServiceTests {

    @Autowired
    private PhoneBookEntryService phoneBookEntryService;

    @Autowired
    private UserService userService;

    @Test
    void phoneBookEntryCreationTest() {
        String phoneNumber = "+37777777777";
        User user = createUserInStorage("Alex");

        PhoneBookEntry entry = new PhoneBookEntry();
        entry.setContactName("Mandy");
        entry.setPhoneNumber(phoneNumber);

        entry = phoneBookEntryService.create(user.getId(), entry);

        String phoneNumberFromStorage = userService.getById(user.getId()).getContacts().get(entry.getId()).getPhoneNumber();
        assertEquals(phoneNumber, phoneNumberFromStorage);
    }

    private User createUserInStorage(String name) {
        User user = new User();
        user.setName(name);
        return userService.create(user);
    }

}
