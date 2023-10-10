package pl.iwaniuk.webapi.controllers;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.CourseNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.exceptions.NoAddmisionException;
import pl.iwaniuk.webapi.models.Course;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.edges.Member;
import pl.iwaniuk.webapi.services.FileService;
import pl.iwaniuk.webapi.services.GroupeService;
import pl.iwaniuk.webapi.services.UserServices;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupeService groupeService;
    @Autowired
    FileService fileService;
    @Autowired
    UserServices userServices;

    //region webapi for groups
    @GetMapping("/")
    public Page<Group> getAllGroups(@RequestParam(defaultValue = "0") int site,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "_id") String type,
                                    @RequestParam(defaultValue = "") String kind,
                                    @RequestParam(defaultValue = "") String owner
                                  ){

        Pageable page = PageRequest.of(site,size, Sort.by(type));

        if(!kind.isBlank() && owner.isBlank()){
            return groupeService.getGroupsByKind(kind,page);
        } else  if (!owner.isBlank() && kind.isBlank()){
            return  groupeService.getGroupsByOwner(owner,page);
        }
        else  if(!kind.isBlank() && !owner.isBlank()){
            return groupeService.getGroupsByOwnerAndKind(kind,owner,page);
        }
        else{
            return groupeService.getAll(page);
        }
    }

    @GetMapping("/{id}")
    public Group getOne(@PathVariable("id")String id) throws GroupNotFoundException {
        Group group = groupeService.getGroupById(id).orElseThrow(()-> new GroupNotFoundException());
        return  group;
    }


    @GetMapping("/me")
    public Page<Group> getGroupsByMemberId() throws GroupNotFoundException {
          return null;
    }

    @PostMapping("/")
    public Group addGroup(  @RequestPart("data") @Valid  Group group,
                            @RequestPart("img") MultipartFile multipartFile)  {
        String src = fileService.saveFile(multipartFile);
        src = "http://localhost:8080/api/files/"+src;
        group.setImg_src(src);
        return  groupeService.create(group);
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable("id")String id,
                          @Valid @RequestPart("data") Group groupReq,
                             @RequestPart(value = "img", required = false) MultipartFile multipartFile) throws GroupNotFoundException {
        Group group = groupeService.getGroupById(id).orElseThrow(()-> new GroupNotFoundException());

        groupReq.setId(group.getId());
        groupReq.setImg_src(group.getImg_src());

        if(multipartFile !=null){
            fileService.deleteFile(group.getImg_src().split("/")[5]);
            String src = fileService.saveFile(multipartFile);
            src = "http://localhost:8080/api/files/"+src;
            groupReq.setImg_src(src);
        }

        return groupeService.update(groupReq);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGroup(@PathVariable("id")String id ) throws GroupNotFoundException {
        Group group = groupeService.getGroupById(id).orElseThrow(()-> new GroupNotFoundException());
        fileService.deleteFile(group.getImg_src().split("/")[5]);
        groupeService.delete(group);

        return noContent().build();
    }


    @GetMapping("/query")
    public Page<Group> query(@RequestParam(defaultValue = "0") int site,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam() String query){

        return groupeService.query(query,PageRequest.of(site,size,Sort.by("id")));
    }
    //endregion

    //region webapi for members

    /*
    * Obsługa edgy, związana z członkostwem usera w danej grupie
    * */

    @PostMapping("/member/join/{id}")
    public ResponseEntity join(@PathVariable("id") String groupId,
            @AuthenticationPrincipal UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws GroupNotFoundException {

        groupeService.join(groupId,usernamePasswordAuthenticationToken.getPrincipal().toString());
        return noContent().build();
    }

    @DeleteMapping("/member/left/")
    public ResponseEntity left(@RequestParam() String groupId,
                               @RequestParam() String userId,
                               @AuthenticationPrincipal UsernamePasswordAuthenticationToken currentUser
                             ) throws NoAddmisionException {
        if(userServices.findUserById(userId).get().getEmail().equals(currentUser.getPrincipal())||
        currentUser.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))||
        groupeService.getGroupById(groupId).get().getOwnerId().equals(userServices.findUserByUserName(currentUser.getPrincipal().toString()).get().getId())){

            groupeService.left(groupId,userId);
            return noContent().build();
        }else{
            throw new NoAddmisionException();
        }

    }

    @PutMapping("/member/accept/")
    public Member accept(@RequestParam() String groupId,
                         @RequestParam() String userId,@AuthenticationPrincipal UsernamePasswordAuthenticationToken currentUser) throws NoAddmisionException {
        if( currentUser.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")) ||
                groupeService.getGroupById(groupId).get().getOwnerId()
                        .equals(
                                userServices.findUserByUserName(
                                        currentUser.getPrincipal().toString()
                                ).get().getId()
                        )
        ) {
            return groupeService.accept(groupId, userId);

        }else{
            throw new NoAddmisionException();
        }
    }

    @GetMapping("/member/getGroups/{id}")
    public Page<Group> getGroupsByMember(@PathVariable("id") String id,
                                         @RequestParam(defaultValue = "0") int site,
                                         @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "true") boolean aacept){
        return groupeService.getGroupByMember(id,site,size,aacept);
    }

    @GetMapping("/member/getMembers/{id}")
    public Page<Object> getMembersByGroupId(@PathVariable("id") String id,
                                            @RequestParam(defaultValue = "0") int site,
                                            @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "true") boolean isAccept){
        return groupeService.getMembersByGroupId(id,isAccept,site,size);
    }

    @GetMapping("/member/isMember/")
    public Object isMember(@RequestParam("groupId") String group_id,
                     @RequestParam("user_id") String user_Id,
                     @RequestParam(defaultValue = "true") boolean aacept){
        return groupeService.isMember(group_id,user_Id,aacept);
    }

    //endregion

    //region for courses in groups

    /*
    * Obsługa edgy, związana z kursami przypisanymi do grupy
    * */

    @PostMapping("/course/add/{id}")
    public ResponseEntity addCourseToGroup(@PathVariable("id") String groupId,
                                           @RequestParam String courseId) throws CourseNotFoundException, GroupNotFoundException {
       groupeService.addCourse(groupId,courseId);

        return noContent().build();
    }

    @DeleteMapping("/course/remove/{id}")
    public ResponseEntity deleteCourseToGroup(@PathVariable("id") String groupId,
                                              @RequestParam String courseId) throws Exception {
        groupeService.deleteCourse(groupId,courseId);

        return noContent().build();
    }

    @GetMapping("/course/{id}")
    public Page<Course> getCourseToGroupByGroupId(@PathVariable("id") String groupID,
                                                  @RequestParam(defaultValue = "0") int site,
                                                  @RequestParam(defaultValue = "10") int size) throws GroupNotFoundException {
        Pageable page = PageRequest.of(site,size);
         return groupeService.getCourseByGroupId(groupID,page);
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<Boolean> chceck(@PathVariable("id") String groupId,
                               @RequestParam String courseId) throws GroupNotFoundException {

        return groupeService.check(groupId,courseId);
    }

    //endregion
}
