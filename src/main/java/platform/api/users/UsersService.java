package platform.api.users;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import platform.api.common.*;
import platform.api.organizations.*;

import java.security.*;
import java.time.*;
import java.util.*;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    @Lazy
    private OrganizationsService organizationsService;

    public Optional<User> getByEmail(String email) {

        return usersRepository.getByEmail(email);

    }

    public Optional<User> updatePrincipalUser(User user, Principal principal) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            principalUser.get().setCharacterId(user.getCharacterId());
//            principalUser.get().setFirstname(user.getFirstname());
//            principalUser.get().setLastname(user.getLastname());

            return Optional.of(usersRepository.save(principalUser.get()));

        }

        return Optional.empty();

    }

    public Optional<User> updateUserByPrincipal(UUID id, User user, Principal principal) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            Optional<User> optionalUser = getByPrincipalOrganizationAndUuid(principal, id);

            if (optionalUser.isPresent()) {

                if (user.getEmail() != null) {

                    optionalUser.get().setEmail(user.getEmail());

                }

                if (user.getFirstname() != null) {

                    optionalUser.get().setFirstname(user.getFirstname());

                }

                if (user.getLastname() != null) {

                    optionalUser.get().setLastname(user.getLastname());

                }

                if (user.getPassword() != null) {

                    optionalUser.get().setPassword(user.getPassword());

                }

                if (user.getFirstname() != null) {

                    optionalUser.get().setFirstname(user.getFirstname());

                }

                if (user.getLastname() != null) {

                    optionalUser.get().setLastname(user.getLastname());

                }

                return Optional.of(usersRepository.save(optionalUser.get()));

            }

        }

        return Optional.empty();

    }

    public Optional<User> getPrincipalUser(Principal principal) {

        return usersRepository.getByEmail(principal.getName());

    }

    public Optional<User> getByUserId(UUID userId) {

        return usersRepository.findById(userId);

    }

    public Optional<User> getByPrincipalOrganizationAndUuid(Principal principal, UUID id) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return usersRepository.getByOrganizationAndId(principalUser.get().getOrganization(), id);

        }

        return Optional.empty();

    }

    public int deleteByPrincipalOrganizationAndId(Principal principal, UUID userId) {

        Optional<User> principalUser = getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return usersRepository.deleteByOrganizationAndId(principalUser.get().getOrganization(), userId);

        }

        return 0;

    }

    public Optional<User> getByEmailAndPassword(String email, String password) {

        return usersRepository.getByEmailAndPassword(email, password);

    }

    // TODO: Add RBAC, cleanup.
    public Optional<User> create(User user, Principal principal) {

        Optional<User> optionalUser = getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            user.setStatus(Status.ACTIVE);
            user.setOrganization(optionalUser.get().getOrganization());
            user.setPassword(user.getNewPassword());

            return Optional.of(usersRepository.save(user));

        }

        return Optional.empty();

    }

    public Optional<User> getUserByPrincipalAndIsAdmin(Principal principal) {

        Optional<User> user = getPrincipalUser(principal);

        if (user.isPresent()) {

            if (user.get().getEmail().equals("root@streamnvr.com")) {

                return user;

            }

        }

        return Optional.empty();

    }

    public Optional<User> changePassword(UserPassword userPassword, Principal principal) {

        Optional<User> optionalUser = getPrincipalUser(principal);

        if (optionalUser.isPresent()) {

            optionalUser.get().setPassword(userPassword.getPassword());

            return Optional.of(usersRepository.save(optionalUser.get()));

        }

        return Optional.empty();

    }

    public Optional<User> setStampLastLoginByEmail(String email) {

        Optional<User> optionalUser = getByEmail(email);

        if (optionalUser.isPresent()) {

            optionalUser.get().setStampLastLogin(LocalDateTime.now());

            return Optional.of(usersRepository.save(optionalUser.get()));

        }

        return Optional.empty();

    }

}
