package platform.api.roles.groups;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.*;
import platform.api.common.*;
import platform.api.organizations.*;
import platform.api.roles.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Data
@Entity
@Table(name = "roles_groups")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class RoleGroup {

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

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @JsonIgnore
    private List<Role> roles;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.ACTIVE;

    private String name;
    private String description;

    @Transient
    public Long organization_id;

}
