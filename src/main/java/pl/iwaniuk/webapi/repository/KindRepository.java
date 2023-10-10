package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Kind;

@Repository
public interface KindRepository extends ArangoRepository<Kind,String> {
}
