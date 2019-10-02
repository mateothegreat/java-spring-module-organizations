package platform.api.users;

import lombok.*;

@Data
public class UserPasswordResetConfirm {

    private String token;
    private String password;

}
