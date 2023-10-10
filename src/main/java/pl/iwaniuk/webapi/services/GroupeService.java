package pl.iwaniuk.webapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import pl.iwaniuk.webapi.exceptions.CourseNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.models.Course;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.edges.Member;

import java.util.Optional;

public interface GroupeService {

    Page<Group> getGroupsByKind(String kind, Pageable pageable);

    Page<Group> getGroupsByOwner(String owner,Pageable pageable);

    Page<Group> getGroupsByOwnerAndKind(String kind,String owner,Pageable pageable);

    Optional<Group> getGroupById(String id);

    Group create(Group group);

    Group update(Group data);

    void delete(Group group);

    Page<Group> getAll(Pageable page);

    void join(String groupId, String username) throws GroupNotFoundException;


    void left(String groupId, String memberId);

    Member accept(String groupId, String userId);

    Page<Group> getGroupByMember(String id, int site, int size, boolean accept);

    Page<Object> getMembersByGroupId(String id, boolean aacept, int site, int size);

    void addCourse(String groupId, String courseId) throws CourseNotFoundException, GroupNotFoundException;

    void deleteCourse(String groupId, String edgeId) throws Exception;

    Page<Course> getCourseByGroupId(String groupID,Pageable pageable) throws GroupNotFoundException;

    Object isMember(String group_id, String user_id, boolean aacept);

    Page<Group> query(String query, PageRequest id);

    ResponseEntity<Boolean> check(String groupId, String courseId);
}
