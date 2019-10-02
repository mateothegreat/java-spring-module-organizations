package platform.api.users;

import io.jsonwebtoken.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {

    private String email;
    private String password;

    public static final long ONE_DAY_MILLIS = 864_000_000; // 1 day

    public static String getJWT(String subject, Long secondsUntilExpire) {

        return Jwts.builder()
                   .setSubject(subject)
                   .setExpiration(new Date(System.currentTimeMillis() + secondsUntilExpire))
                   .signWith(SignatureAlgorithm.HS512, System.getenv("JWT_SECRET"))
                   .compact();

    }

}
