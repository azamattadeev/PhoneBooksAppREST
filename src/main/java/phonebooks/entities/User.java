package phonebooks.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

@Data
@EqualsAndHashCode
public class User implements Serializable {

    private Long id;

    @NotNull(message = "Please enter user's name")
    @NotEmpty(message = "Please enter user's name")
    private String name;

    private HashMap<Long, PhoneBookEntry> contacts;

    public User() {
        contacts = new HashMap<>();
    }

    public User(String name) {
        this();
        this.name = name;
    }

}