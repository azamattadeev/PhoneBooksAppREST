package phonebooks.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class PhoneBookEntry implements Serializable {

    private Long id;

    @NotNull(message = "Please enter user's name")
    @NotEmpty(message = "Please enter user's name")
    private String contactName;

    @NotNull(message = "Please enter user's name")
    @NotEmpty(message = "Please enter user's name")
    private String phoneNumber;

}