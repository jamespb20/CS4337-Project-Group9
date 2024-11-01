package cs4337.group9.mediumwebsite.Entity;
import cs4337.group9.mediumwebsite.enums.Status;
import cs4337.group9.mediumwebsite.enums.Role;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;  // This should store a hashed password
    private Role role;
    private Status status;




}
