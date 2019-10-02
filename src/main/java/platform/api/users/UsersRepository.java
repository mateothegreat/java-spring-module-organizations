package platform.api.users;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.*;
import platform.api.organizations.*;

import java.util.*;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, UUID> {

    Page<User> getByOrganization(Organization organization, Pageable pageable);

    User getByUsername(String username);

    @Transactional
    @Query(value = "SELECT * FROM users u WHERE (u.firstname LIKE :term1 OR u.lastname LIKE :term2 OR u.email LIKE :term3) AND u.status = :status ORDER BY u.id DESC", nativeQuery = true)
    Page<User> _search(@Param("term1") String term1, @Param("term2") String term2, @Param("term3") String term3, @Param("status") int status, Pageable pageable);

    @Query(value = "SELECT COUNT(u.id) FROM users u WHERE u.status = :status", nativeQuery = true)
    Integer _stats(@Param("status") int status);

    Optional<User> getByEmail(String email);

    Optional<User> getByEmailAndPassword(String email, String password);

    Optional<User> getByConfirmEmailToken(String confirmEmailToken);

    Optional<User> getByPasswordResetToken(String passwordResetToken);

    Optional<User> getByOrganizationAndId(Organization organization, UUID id);

    @Transactional
    @Modifying
    int deleteByOrganizationAndId(Organization organization, UUID id);

}
