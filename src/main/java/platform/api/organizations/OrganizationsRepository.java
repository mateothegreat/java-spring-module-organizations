package platform.api.organizations;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface OrganizationsRepository extends PagingAndSortingRepository<Organization, UUID> {

}
