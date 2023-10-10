package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.CourseNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.exceptions.PostNotFoundException;
import pl.iwaniuk.webapi.models.Course;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.Lesson;
import pl.iwaniuk.webapi.models.Post;
import pl.iwaniuk.webapi.repository.MemberRepository;
import pl.iwaniuk.webapi.services.CourseService;
import pl.iwaniuk.webapi.services.FileService;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    FileService fileService;



    @GetMapping("/")
    public Page<Course> getAllCousrses(@RequestParam(defaultValue = "0") int site,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "_id") String type,
                                     @RequestParam(defaultValue = "") String kindID,
                                     @RequestParam(defaultValue = "") String authorID,
                                  //     @RequestParam(defaultValue = "")String groupID,
                                       @RequestParam(defaultValue = "-1") int  published
    ){

        Pageable page = PageRequest.of(site,size, Sort.by(type));

        if(published < 0){
            if(!kindID.isBlank()&&authorID.isBlank()){
                return courseService.getAllByKindID(kindID,page);
            }else if(!kindID.isBlank()&&!authorID.isBlank()){
                return courseService.getAllByKindIDAndAuthorID(kindID,authorID,page);
            }else if(kindID.isBlank()&&!authorID.isBlank()){
                return courseService.getAllByAuthorID(authorID,page);
            }else{
                return courseService.getAll(page);
            }

        }
        else if(published ==0){
            if(!kindID.isBlank()&&authorID.isBlank()){
                return courseService.getAllByKindIDAndPublished(false,kindID,page);
            }else if(!kindID.isBlank()&&!authorID.isBlank()){
                return courseService.getAllByKindIDAndAuthorIDAndPublished(false,kindID,authorID,page);
            }else if(kindID.isBlank()&&!authorID.isBlank()){
                return courseService.getAllByAuthorIDAndPublished(false,authorID,page);
            }else{
                return courseService.getAllByPublished(false,page);
            }

        }
        else if(published > 0) {
            if(!kindID.isBlank()&&authorID.isBlank()){
                return courseService.getAllByKindIDAndPublished(true,kindID,page);
            }else if(!kindID.isBlank()&&!authorID.isBlank()){
                return courseService.getAllByKindIDAndAuthorIDAndPublished(true,kindID,authorID,page);
            }else if(kindID.isBlank()&&!authorID.isBlank()){
                return courseService.getAllByAuthorIDAndPublished(true,authorID,page);
            }else{
                return courseService.getAllByPublished(true,page);
            }
        }

        return null;
    }

    @GetMapping("/{id}")
    public Course getOne(@PathVariable("id")String id) throws CourseNotFoundException {
        Course course = courseService.getCourseById(id).orElseThrow(CourseNotFoundException::new);
        return  course;
    }

    @PostMapping("/")
    public Course addOne(@Valid @RequestPart("data") Course course,
                         @RequestPart("files")List<MultipartFile> multipartFiles) throws  GroupNotFoundException {

        return courseService.create(course,multipartFiles);
    }

    @PutMapping("/{id}")
    public Course updateOne(@PathVariable("id")String id,@RequestPart("data") Course courseReq,
                            @RequestPart("files")List<MultipartFile> multipartFiles) throws CourseNotFoundException {


        return  courseService.update(courseReq,multipartFiles,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOne(@PathVariable("id")String id) throws CourseNotFoundException {
        Course course = courseService.getCourseById(id).orElseThrow(CourseNotFoundException::new);
        courseService.delete(course);

        return    noContent().build();
    }


    @GetMapping("/query")
    public Page<Course> getCoursesByQuery(@RequestParam(defaultValue = "0") int site,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam() String query){
        var result =  courseService.query(query,PageRequest.of(site,size));
         return result;
    }
}
