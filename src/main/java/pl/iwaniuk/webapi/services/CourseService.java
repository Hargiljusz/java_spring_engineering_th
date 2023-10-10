package pl.iwaniuk.webapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.CourseNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.models.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {


    Optional<Course> getCourseById(String id);

    Course create(Course course, List<MultipartFile> multipartFiles) throws GroupNotFoundException;

    Course update(Course courseReq, List<MultipartFile> multipartFiles, String id) throws CourseNotFoundException;

    void delete(Course course);

    Page<Course> getAllByKindID(String kindID, Pageable page);

    Page<Course> getAllByKindIDAndAuthorID(String kindID, String authorID, Pageable page);

    Page<Course> getAllByAuthorID(String authorID, Pageable page);

    Page<Course> getAll(Pageable page);

    Page<Course> getAllByKindIDAndPublished(boolean b, String kindID, Pageable page);

    Page<Course> getAllByKindIDAndAuthorIDAndPublished(boolean b, String kindID, String authorID, Pageable page);

    Page<Course> getAllByAuthorIDAndPublished(boolean b, String authorID, Pageable page);

    Page<Course> getAllByPublished(boolean b, Pageable page);

    Page<Course> query(String query, PageRequest id);
}
