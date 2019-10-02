package platform.api.users.roles;

import lombok.*;
import platform.api.users.*;

import javax.persistence.*;

@Data
public class UserRole {

    private String name;
    private String apiName;

    @ManyToOne
    private User user;

}
