package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ArangoRepository<pl.iwaniuk.webapi.models.User,String> {

    Optional<pl.iwaniuk.webapi.models.User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByConfim(Pageable pageable,boolean confirm);

    List<User> findByNameContainingIgnoreCaseOrSurenameContainingIgnoreCase(String name,String sName);
    Page<User> findByNameContainingIgnoreCaseOrSurenameContainingIgnoreCaseOrIdOrEmail(String name,String sName,String id,String email,Pageable pageable);
}
