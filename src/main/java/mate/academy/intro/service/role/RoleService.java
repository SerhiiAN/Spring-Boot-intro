package mate.academy.intro.service.role;

import java.util.Optional;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.RoleName;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleService {
    Optional<Role> findRoleByName(RoleName roleName);
}
