package pl.iwaniuk.webapi.services;

import pl.iwaniuk.webapi.models.Role;

public interface RoleService {
    Role  findRoleByName(String name);
}
