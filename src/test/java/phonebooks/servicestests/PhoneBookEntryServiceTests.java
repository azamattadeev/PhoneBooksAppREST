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
    @Transactional
    void phoneBookEntryCreationTest() {
        String phoneNumber = "+37777777777";
        User user = createUserInDb("Alex");

        PhoneBookEntry entry1 = new PhoneBookEntry();
        entry1.setContactName("Mandy");
        entry1.setPhoneNumber(phoneNumber);

        phoneBookEntryService.create(user.getId(), entry1);
        assertEquals(userService.getById(user.getId()).getContacts().get(0).getPhoneNumber(), phoneNumber);
    }

    private User createUserInDb(String name) {
        User user = new User();
        user.setName(name);
        return userService.create(user);
    }

}
