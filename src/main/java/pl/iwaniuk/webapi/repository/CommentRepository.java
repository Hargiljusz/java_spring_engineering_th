package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Comment;
import pl.iwaniuk.webapi.models.Group;

import java.util.List;

@Repository
public interface CommentRepository extends ArangoRepository<Comment,String> {

    List<Comment> findByConntentIsContainingIgnoreCase(String query);

    List<Comment> findByAuthorId(String id);
}
