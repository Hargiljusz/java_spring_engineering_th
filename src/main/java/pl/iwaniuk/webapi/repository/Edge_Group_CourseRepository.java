package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import pl.iwaniuk.webapi.models.edges.Edge_Group_Course;

import java.util.Optional;

public interface Edge_Group_CourseRepository extends ArangoRepository<Edge_Group_Course,String> {

    Optional<Edge_Group_Course> findByCourse_Id(String id);
    boolean existsByGroup_IdAndCourse_Id(String gId,String cId);

    Optional<Edge_Group_Course> findByGroup_IdAndCourse_Id(String gId,String cId);

    void deleteAllByGroup_Id(String groupId);
}
