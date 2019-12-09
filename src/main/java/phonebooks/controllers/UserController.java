package phonebooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phonebooks.entities.User;
import phonebooks.services.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity addUser(@Valid @RequestBody User user) {
        user = userService.create(user);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PutMapping("{id}")
    public ResponseEntity updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody User user
    ) {
        user = userService.update(id, user);
        if (user != null) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
