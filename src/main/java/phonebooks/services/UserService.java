package phonebooks.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phonebooks.entities.User;
import phonebooks.repositories.UserRepository;

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

    public User getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User update(User user) {
        return repository.save(user);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
