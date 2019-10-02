package platform.api.roles;

import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;
import platform.api.roles.groups.*;


@Repository
public interface RolesRepository extends PagingAndSortingRepository<Role, Long> {

    Page<Role> getByOrganization_id(Long organizationId, Pageable pageable);

    Page<Role> getByRoleGroupsContains(RoleGroup roleGroup, Pageable pageable);

//    List<Role> getByUser(User user);

}
