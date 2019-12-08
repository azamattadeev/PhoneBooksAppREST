package phonebooks.controllerstests;

import org.junit.jupiter.api.BeforeEach;
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
import phonebooks.controllers.UserController;
import phonebooks.entities.User;
import phonebooks.repositories.UserRepository;
import phonebooks.services.UserService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static phonebooks.ObjectMapping.mapFromJson;
import static phonebooks.ObjectMapping.mapToJson;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, UserService.class, UserRepository.class})
@SpringBootTest(classes = PhonebooksApplication.class)
public class CRUDUserControllerTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final String USER_URI = "/user/";

    private User[] users = {
            new User("Alex"),
            new User("John"),
            new User("Rachel"),
            new User("Jennifer"),
            new User("Paul"),
            new User("Marcus")
    };

    @BeforeEach
    void addUserToDb(){
        Arrays.asList(users).forEach(userService::create);
    }

    @Test
    void createUserAndGetAfterThatTest() throws Exception {
        User user = new User();
        user.setName("John");

        String userJson = mapToJson(user);

        // Creating a new user
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andReturn();

        String newUserUrl = mvcResult.getResponse().getHeader("Location");

        // Checking that response code is 201 (Created)
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertNotNull(newUserUrl);

        // Getting the user that was created
        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(newUserUrl)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        User newUser = mapFromJson(mvcResult.getResponse().getContentAsString(), User.class);

        assertNotNull(newUser.getId());
        assertEquals(user.getName(), newUser.getName());
    }

    @Test
    void updateUserTest() throws Exception {
        Long id = userService.getAll().get(0).getId();
        String newName = "New name";
        User user = new User(newName);

        // Updating user's name via UserController
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(USER_URI + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(user))
        ).andReturn();

        // Checking that response code is 204 (No Content)
        assertEquals(204, mvcResult.getResponse().getStatus());
        // Checking new name
        assertEquals(newName, userService.getById(id).getName());
    }

    @Test
    void deleteUserTest() throws Exception {
        Long id = userService.getAll().get(0).getId();

        // Deleting user via UserController
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete(USER_URI + id)
        ).andReturn();

        // Checking that response code is 204 (No Content)
        assertEquals(204, mvcResult.getResponse().getStatus());
        // Checking result of Deletion
        assertNull(userService.getById(id));
    }

    @Test
    void getAllUsersTest() throws Exception {
        // Getting all users via UserController
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        // Checking that the response code is OK (200)
        assertEquals(200, mvcResult.getResponse().getStatus());

        User[] usersFromResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), User[].class);

        // Checking that the number of users in response is equal number of users in repository
        assertEquals(userRepository.findAll().size(), usersFromResponse.length);
    }

    @Test
    void getNoneExistentUser404Test() throws Exception{
        long noneExistentId = -1181176574;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(USER_URI + noneExistentId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        // Checking that response code is 404 (Not Found)
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

}
