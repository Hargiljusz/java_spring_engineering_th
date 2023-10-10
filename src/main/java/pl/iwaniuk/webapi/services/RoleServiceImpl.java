package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iwaniuk.webapi.models.Role;
import pl.iwaniuk.webapi.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByRole(name);
    }
}
