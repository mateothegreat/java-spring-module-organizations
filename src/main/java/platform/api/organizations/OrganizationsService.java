package platform.api.organizations;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import platform.api.organizations.links.*;
import platform.api.users.*;

import javax.transaction.*;
import java.security.*;
import java.util.*;

@Service
@Transactional
public class OrganizationsService {

    private OrganizationsRepository  organizationsRepository;
    private OrganizationLinksService organizationLinksService;

    private UsersService usersService;

    @Autowired
    public OrganizationsService(final OrganizationsRepository organizationsRepository,
                                final OrganizationLinksService organizationLinksService,
                                final UsersService usersService) {

        this.organizationsRepository = organizationsRepository;
        this.organizationLinksService = organizationLinksService;
        this.usersService = usersService;

    }

    public Optional<Organization> getById(UUID id) {

        return organizationsRepository.findById(id);

    }

    public Optional<Organization> createByPrincipal(Organization organization, Principal principal) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            Organization createdOrganization = organizationsRepository.save(organization);

            organizationLinksService.createLink(principalUser.get().getOrganization(), createdOrganization);

            return Optional.of(createdOrganization);

        }

        return Optional.empty();

    }

}
