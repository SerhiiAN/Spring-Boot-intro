package mate.academy.intro.service.role;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.RoleName;
import mate.academy.intro.repository.user.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }
}
