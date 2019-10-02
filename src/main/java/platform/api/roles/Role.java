package platform.api.roles;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.*;
import platform.api.common.*;
import platform.api.organizations.*;
import platform.api.roles.groups.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Data
@Entity
@Table(name = "roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Role {

    public static final String ROLE_ADMIN     = "ADMIN";
    public static final String ROLE_READ_ONLY = "READ_ONLY";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Range(max = 4294967295L)
    @Column(nullable = false, updatable = false, unique = true)
    public Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    @JsonIgnore
    private Organization organization;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.ACTIVE;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    @JsonIgnore
    private List<RoleGroup> roleGroups;

    private String name;
    private String description;
    private String apiName;

}
