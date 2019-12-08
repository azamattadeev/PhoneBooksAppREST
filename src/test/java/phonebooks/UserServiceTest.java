package phonebooks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import phonebooks.entities.User;
import phonebooks.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void userCreationTest() {
        User user = createSam();
        User queriedUser = userService.getById(user.getId());
        assertTrue(user.getId().equals(queriedUser.getId()) && user.getName().equals(queriedUser.getName()));
    }

    @Test
    void userUpdateTest() {
        User user = createSam();
        Long id = user.getId();
        user.setName("Alex");
        userService.update(user);
        assertEquals(userService.getById(id).getName(), "Alex");
    }

    @Test
    void userDeleteTest() {
        User user = createSam();
        Long id = user.getId();
        userService.deleteById(id);
        assertNull(userService.getById(id));
    }

    private User createSam() {
        User user = new User();
        user.setName("Sam");
        return userService.create(user);
    }

}
