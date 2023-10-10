package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Course;

import java.util.List;

@Repository
public interface CourseRepository extends ArangoRepository<Course,String> {
    Page<Course> findByKind_Id(String kind_id,Pageable pageable);
    Page<Course> findByKind_IdAndAuthorId(String kind_id,String authorID,Pageable pageable);
    Page<Course> findByAuthorId(String authorID,Pageable pageable);

    Page<Course> findByKind_IdAndPublished(String kind_id,boolean published,Pageable pageable);
    Page<Course> findByKind_IdAndAuthorIdAndPublished(String kind_id,String authorID,boolean published,Pageable pageable);
    Page<Course> findByAuthorIdAndPublished(String authorID,boolean published,Pageable pageable);
    Page<Course> findByPublished(boolean published,Pageable pageable);

    Page<Course> findByGroupsId(String id,Pageable pageable);

    List<Course> findByPublishedOrNameIsContainingIgnoreCaseOrKind_NameIsContainingIgnoreCaseOrDecriptionIsContainingIgnoreCase(boolean published,String name, String kName, String desc);
    List<Course> findByAuthorIdAndPublished(String authorID,boolean pub);

}
