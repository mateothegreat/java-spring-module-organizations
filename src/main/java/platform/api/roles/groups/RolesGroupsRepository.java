package platform.api.roles.groups;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesGroupsRepository extends PagingAndSortingRepository<RoleGroup, Long> {

}
