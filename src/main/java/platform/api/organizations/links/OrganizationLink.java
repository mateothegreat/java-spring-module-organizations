package platform.api.organizations.links;


import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.*;
import platform.api.organizations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@Data
@Entity
@Table(name = "organization_links")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrganizationLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Range(max = 4294967295L)
    @Column(nullable = false, updatable = false, unique = true)
    public Long id;

    @OneToOne
    private Organization parent;

    @OneToOne
    private Organization child;

}
