package mate.academy.intro.repository.user;

import java.util.Optional;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
