package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Role;

@Repository
public interface RoleRepository extends ArangoRepository <Role,String> {
    Role findByRole(String role);
}
