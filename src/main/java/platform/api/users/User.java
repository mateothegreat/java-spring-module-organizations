package platform.api.users;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import platform.api.common.*;
import platform.api.organizations.*;
import platform.api.roles.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class
User implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @JsonIgnore
    private UUID token;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private Organization organization;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @JsonIgnore
    private List<Role> roles;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    private String email;
    private String username;
    private String role;

    @JsonIgnore
    private String password;

    @Transient
    private String newPassword;

    private String firstname;
    private String lastname;
    private String characterId;

    @CreationTimestamp
    private LocalDateTime stampCreated;

    public LocalDateTime stampLastLogin;

    @JsonIgnore
    private String passwordResetToken;

    @JsonIgnore
    private String confirmEmailToken;

    @JsonIgnore
    private Boolean isConfirmed;
    @JsonIgnore
    private Boolean isAdmin;

    @JsonIgnore
    private Boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
