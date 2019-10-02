package platform.api.organizations.links;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;
import platform.api.organizations.*;

@Repository
public interface OrganizationLinksRepository extends PagingAndSortingRepository<OrganizationLink, Long> {

    int deleteByChildAndParent(Organization child, Organization parent);

}
