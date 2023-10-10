package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.iwaniuk.webapi.exceptions.CourseNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.models.Course;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.models.edges.Edge_Group_Course;
import pl.iwaniuk.webapi.models.edges.Member;
import pl.iwaniuk.webapi.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupeServiceImpl implements GroupeService {

    @Autowired
    GroupeRepository groupeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    Edge_Group_CourseRepository edge_group_courseRepository;

    //region groups

    @Override
    public Page<Group> getGroupsByKind(String kind, Pageable pageable) {
        return groupeRepository.findByKind_Id(pageable, kind);
    }

    @Override
    public Page<Group> getGroupsByOwner(String owner, Pageable pageable) {

        return groupeRepository.findAllByOwnerId(pageable, owner);
    }

    @Override
    public Page<Group> getGroupsByOwnerAndKind(String kind, String owner, Pageable pageable) {

        return groupeRepository.findAllByOwnerIdAndKind_Id(pageable, kind, owner);
    }

    @Override
    public Optional<Group> getGroupById(String id) {
        return groupeRepository.findById(id);
    }

    @Override
    public Group create(Group group) {
        return groupeRepository.save(group);
    }

    @Override
    public Group update(Group data) {
        return groupeRepository.save(data);
    }

    @Override
    public void delete(Group group) {
        memberRepository.deleteAllByGroup_Id(group.getId());
        edge_group_courseRepository.deleteAllByGroup_Id(group.getId());
        groupeRepository.delete(group);
    }

    @Override
    public Page<Group> getAll(Pageable page) {
        return groupeRepository.findAll(page);
    }

    //endregion

    //members

    @Override
    public void join(String groupId, String username) throws GroupNotFoundException {
        User user = userRepository.findByEmail(username).get();
        Group group = groupeRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);

        memberRepository.save(new Member(user, group, false));
    }

    @Override
    public void left(String groupId, String userId) {
        Member member = memberRepository.findByGroup_IdAndUser_Id(groupId, userId).get();
        memberRepository.delete(member);
    }

    @Override
    public Member accept(String groupId, String userId) {

        Member member = memberRepository.findByGroup_IdAndUser_Id(groupId, userId).get();
        member.setAccept(true);
        return memberRepository.save(member);
    }

    @Override
    public Page<Group> getGroupByMember(String id, int site, int size, boolean accept) {
        Page<Group> groupePage = memberRepository.findByUser_Id(
                id,
                PageRequest.of(site, size,Sort.by("isAccept"))).map(m -> m.getGroup()
        );

        return groupePage;
    }

    @Override
    public Page<Object> getMembersByGroupId(String id, boolean aacept, int site, int size) {
        Page<Object> objects = memberRepository.findByGroup_IdAndIsAccept(
                id,
                aacept,
                PageRequest.of(site, size, Sort.by("surname").and(Sort.by("name")))).map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("email", m.getUser().getEmail());
            map.put("id",m.getUser().getId());
            map.put("name", m.getUser().getName());
            map.put("surname", m.getUser().getSurename());
            map.put("phonenumber", m.getUser().getPhoneNumber());
            return map;
        });
        return objects;
    }

    @Override
    public Object isMember(String group_id, String user_id, boolean aacept) {
        Map<String, Boolean> model = new HashMap<>();
        model.put("isJoin", memberRepository.existsByGroup_IdAndUser_Id(group_id, user_id));

        var result = memberRepository.findByGroup_IdAndUser_Id(group_id, user_id);
        if (result.isPresent()) {
            model.put("isMember", result.get().isAccept());
        }


        return model;
    }

    @Override
    public Page<Group> query(String query, PageRequest id) {
        List<Group> result = groupeRepository.findByNameIsContainingIgnoreCaseOrKind_NameIsContainingIgnoreCase(query,query);
        List<User> users = userRepository.findByNameContainingIgnoreCaseOrSurenameContainingIgnoreCase(query,query);
        List<Group>result2  = new ArrayList<>();
        users.forEach(u->{
            result2.addAll(groupeRepository.findByOwnerId(u.getId()));
        });

        result2.addAll(groupeRepository.findByOwnerId(query));

        result.addAll(result2);
        result = result.stream().distinct().collect(Collectors.toList());

        int start = (int)id.getOffset();
        int end = (start + id.getPageSize()) > result.size() ? result.size() : (start + id.getPageSize());
        return new PageImpl<Group>(result.subList(start,end),id,result.size());
    }

    @Override
    public ResponseEntity<Boolean> check(String groupId, String courseId) {
        return ResponseEntity.ok(edge_group_courseRepository.existsByGroup_IdAndCourse_Id(groupId,courseId));
    }
    //endregion

    //region courses
    @Override
    public void addCourse(String groupId, String courseId) throws CourseNotFoundException, GroupNotFoundException {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException();
        }
        if (!groupeRepository.existsById(groupId)) {
            throw new GroupNotFoundException();
        }
        if(!edge_group_courseRepository.existsByGroup_IdAndCourse_Id(groupId,courseId)) {
            edge_group_courseRepository.save(new Edge_Group_Course(groupeRepository.findById(groupId).get(), courseRepository.findById(courseId).get()));
        }

    }

    @Override
    public void deleteCourse(String groupId, String courseID) throws Exception {
        edge_group_courseRepository.delete(edge_group_courseRepository.findByGroup_IdAndCourse_Id(groupId,courseID).orElseThrow(Exception::new));
    }

    @Override
    public Page<Course> getCourseByGroupId(String groupID, Pageable pageable) throws GroupNotFoundException {
        if (!groupeRepository.existsById(groupID)) {
            throw new GroupNotFoundException();
        }
        return courseRepository.findByGroupsId(groupID, pageable);
    }


//endregion

}
