package phonebooks.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phonebooks.entities.User;
import phonebooks.repositories.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {
    private UserRepository repository;

    public UserService (UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        return repository.save(user);
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User update(Long id, User user) {
        if(repository.existsById(id)) {
            user.setId(id);
            return repository.save(user);
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

}
