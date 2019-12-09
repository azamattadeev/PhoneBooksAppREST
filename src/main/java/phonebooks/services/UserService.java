package phonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phonebooks.entities.User;
import phonebooks.stores.UserStorage;

import java.util.List;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService (UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.saveUser(user);
    }

    public List<User> getAll(){
        return userStorage.getAllUsers();
    }

    public User getById(Long id) {
        return userStorage.getUserById(id);
    }

    public User update(Long id, User user) {
        if(userStorage.existsUserById(id)) {
            user.setId(id);
            return userStorage.updateUser(user);
        } else {
            return null;
        }
    }

    public boolean deleteById(Long id) {
        if (userStorage.existsUserById(id)) {
            userStorage.deleteUserById(id);
            return true;
        } else {
            return false;
        }
    }

}
