package platform.api.users;

import lombok.*;

@Data
public class UserNotificationPreference {

    private String  preferenceName;
    private Boolean enabled;

}
