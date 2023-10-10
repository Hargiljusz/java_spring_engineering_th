package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Event;

@Repository
public interface EventRepository extends ArangoRepository<Event,String> {
}
