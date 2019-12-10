package phonebooks.controllerstests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import phonebooks.PhonebooksApplication;
import phonebooks.controllers.PhoneBookEntryController;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.entities.User;
import phonebooks.services.PhoneBookEntryService;
import phonebooks.stores.UserStorage;

import static org.junit.jupiter.api.Assertions.*;
import static phonebooks.ObjectMapping.mapFromJson;
import static phonebooks.ObjectMapping.mapToJson;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {PhoneBookEntryController.class, PhoneBookEntryService.class, UserStorage.class})
@SpringBootTest(classes = PhonebooksApplication.class)
public class PhoneBookEntryTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private UserStorage userStorage;

    private final String PHONE_BOOK_ENTRY_URI = "/phone-book-entry/";

    @Test
    void createAndGetPhoneBookEntryTest() throws Exception{
        Long id = userStorage.getAllUsers().get(1).getId();

        var entry = new PhoneBookEntry("Ander", "+21212121");

        // Creating Phone book entry via PhoneBookEntryController
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/user/" + id + "/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(entry))
        ).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        assertNotNull(location);

        // Getting entry via PhoneBookEntryController
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(location)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        var returnedEntry = mapFromJson(mvcResult.getResponse().getContentAsString(), PhoneBookEntry.class);

        assertEquals(entry.getContactName(), returnedEntry.getContactName());
        assertEquals(entry.getPhoneNumber(), returnedEntry.getPhoneNumber());

        User user = userStorage.getUserById(id);

        entry.setId(returnedEntry.getId());

        returnedEntry = user.getContacts().get(returnedEntry.getId());

        assertEquals(entry, returnedEntry);
    }

    @Test
    void updatePhoneBookEntryTest() throws Exception{
        User user = createUserInStorage("Daniel");
        PhoneBookEntry entry = createPhoneBookEntryInStorage(
                user.getId(), "Mason", "+2001");

        String newNumber = "+3000";
        var newEntry = new PhoneBookEntry("Mason", newNumber);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(PHONE_BOOK_ENTRY_URI + entry.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(newEntry))
        ).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());

        assertEquals(entry.getPhoneNumber(), newNumber);

    }

    @Test
    void deletePhoneBookEntryTest() throws Exception{
        User user = createUserInStorage("Roy");
        PhoneBookEntry entry = createPhoneBookEntryInStorage(
                user.getId(), "Patrick", "+3007");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete(PHONE_BOOK_ENTRY_URI + entry.getId())
        ).andReturn();

        // Checking that deleting is successful
        assertEquals(204, mvcResult.getResponse().getStatus());

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(PHONE_BOOK_ENTRY_URI + entry.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        // Checking that entry is deleted
        assertEquals(404, mvcResult.getResponse().getStatus());

    }

    @Test
    void getAllPhoneBookEntriesByUserTest() throws Exception{
        User andy = createUserInStorage("Andy");
        User louis = createUserInStorage("Louis");

        PhoneBookEntry louisContact1 = createPhoneBookEntryInStorage(
                louis.getId(), "Sam" , "+333");
        PhoneBookEntry louisContact2 = createPhoneBookEntryInStorage(
                louis.getId(), "Sarah", "+545");
        PhoneBookEntry andyContact = createPhoneBookEntryInStorage(
                andy.getId(), "Skyler", "+22222");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/user/" + louis.getId() + "/contacts/")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        PhoneBookEntry[] louisContacts = mapFromJson(mvcResult.getResponse().getContentAsString(), PhoneBookEntry[].class);

        assertTrue(louisContact1.equals(louisContacts[0])
                || louisContact2.equals(louisContacts[0]));
        assertTrue(louisContact1.equals(louisContacts[1])
                || louisContact2.equals(louisContacts[1]));

    }

    @Test
    void getAllPhoneBookEntriesByPhoneNumber() throws Exception {
        User user = createUserInStorage("Sam");
        String numberForSearch = "++332211";

        PhoneBookEntry entry1 = createPhoneBookEntryInStorage(
                user.getId(), "sad", numberForSearch
        );
        PhoneBookEntry entry2 = createPhoneBookEntryInStorage(
                user.getId(), "saf", numberForSearch
        );
        PhoneBookEntry entry3 = createPhoneBookEntryInStorage(
                user.getId(), "saf", "e124"
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(PHONE_BOOK_ENTRY_URI + "phone-number/" + numberForSearch)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        PhoneBookEntry[] entries = mapFromJson(mvcResult.getResponse().getContentAsString(), PhoneBookEntry[].class);

        assertEquals(2, entries.length);

        assertEquals(numberForSearch, entries[0].getPhoneNumber());
        assertEquals(numberForSearch, entries[1].getPhoneNumber());
    }

    private User createUserInStorage(String name){
        var user = new User(name);
        return userStorage.saveUser(user);
    }

    private PhoneBookEntry createPhoneBookEntryInStorage(Long userId, String contactName, String phoneNumber) {
        return userStorage.savePhoneBookEntryForUser(userId, new PhoneBookEntry(contactName, phoneNumber));
    }

}
