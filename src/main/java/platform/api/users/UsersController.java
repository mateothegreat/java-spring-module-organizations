package platform.api.users;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.web.bind.annotation.*;
import platform.api.common.*;
import platform.api.logging.*;
import platform.api.organizations.*;
import platform.api.organizations.links.*;
import platform.api.roles.*;

import java.security.*;
import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Value("${BASE_URL:http://localhost:4204}")
    private String baseUrl;

    private       UsersRepository          repository;
    private       OrganizationsRepository  organizationsRepository;
    private final OrganizationsService     organizationsService;
    private       BCryptPasswordEncoder    bCryptPasswordEncoder;
    private       UsersService             usersService;
    private       LogsService              logsService;
    private       RolesService             rolesService;
    private final OrganizationLinksService organizationLinksService;

    @Autowired
    public UsersController(final UsersRepository repository,
                           final OrganizationsRepository organizationsRepository,
                           final OrganizationsService organizationsService,
                           final UsersService usersService,
                           final LogsService logsService,
                           final RolesService rolesService,
                           final OrganizationLinksService organizationLinksService) {

        this.repository = repository;
        this.organizationsRepository = organizationsRepository;
        this.organizationsService = organizationsService;
        this.usersService = usersService;
        this.logsService = logsService;
        this.rolesService = rolesService;
        this.organizationLinksService = organizationLinksService;

    }

    @GetMapping("/byorg")
    public ResponseEntity<?> getAll(Principal principal, Pageable pageable) {

        Optional<User> principalUser = usersService.getPrincipalUser(principal);

        if (principalUser.isPresent()) {

            return new ResponseEntity<>(repository.getByOrganization(principalUser.get().getOrganization(), pageable), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

    @GetMapping(Patterns.UUIDv4)
    public ResponseEntity<User> getById(@PathVariable("uuid") UUID id, Principal principal) {

        Optional<User> user = usersService.getByPrincipalOrganizationAndUuid(principal, id);
//        Optional<User> user = usersService.getByUuid(id);

        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping(path = "/{id}/logs")
    public ResponseEntity<?> getLogsById(@PathVariable("id") UUID id, Pageable pageable, Principal principal) {

        if (rolesService.userHasRole(principal, Role.ROLE_ADMIN)) {

            try {

                Optional<User> user = repository.findById(id);

                if (user.isPresent()) {

                    return new ResponseEntity<>(logsService.getByUser(user.get().getId(), pageable), HttpStatus.OK);

                } else {

                    return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);

                }

            } catch (EmptyResultDataAccessException ex) {

                return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);

            }

        } else {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) {

        Optional<User> user = usersService.getByEmailAndPassword(userLogin.getEmail(),
                                                                 userLogin.getPassword());

        if (user.isPresent()) {

            if (user.get().getStatus().equals(Status.ACTIVE)) {

                logsService.createForUser(user.get().getId(), "LOGIN_SUCCESSFUL", "User logged in.");

                user.get().setStampLastLogin(LocalDateTime.now());

                repository.save(user.get());

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_OK, UserLogin.getJWT(userLogin.getEmail(), UserLogin.ONE_DAY_MILLIS)), HttpStatus.OK);

            } else if (user.get().getStatus().equals(Status.PENDING)) {

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR,
                                                              "Your email address has not been confirmed. Please check your email or contact us.",
                                                              Status.PENDING), HttpStatus.OK);

            } else {

                return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Invalid email address and/or password."), HttpStatus.OK);

            }

        } else {

            return new ResponseEntity<>(new RequestResult(RequestResult.RESULT_ERROR, "Invalid email address and/or password."), HttpStatus.OK);

        }

    }

    @DeleteMapping(Patterns.UUIDv4)
    public ResponseEntity<?> delete(@PathVariable("uuid") UUID id, Principal principal) {

        return new ResponseEntity<>(usersService.deleteByPrincipalOrganizationAndId(principal, id), HttpStatus.OK);

    }

    @GetMapping(path = "/my")
    public ResponseEntity<User> getCurrentUser(Principal principal) {

        return new ResponseEntity<>(repository.getByEmail(principal.getName()).get(), HttpStatus.OK);

    }

    @PutMapping(path = "/my")
    public ResponseEntity<User> update(@RequestBody User user, Principal principal) {

        return new ResponseEntity<>(usersService.updatePrincipalUser(user, principal).get(), HttpStatus.OK);

    }

    @PutMapping(Patterns.UUIDv4)
    public ResponseEntity<User> updateByUserId(@PathVariable("uuid") UUID id, @RequestBody User user, Principal principal) {

        return new ResponseEntity<>(usersService.updateUserByPrincipal(id, user, principal).get(), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user, Principal principal) {

        Optional<User> optionalUser = usersService.create(user, principal);

        return optionalUser.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                           .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));


    }

    @PostMapping("/password")
    public ResponseEntity<User> changePassword(@RequestBody UserPassword userPassword, Principal principal) {

        Optional<User> optionalUser = usersService.changePassword(userPassword, principal);

        return optionalUser.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.FORBIDDEN));

    }

}
