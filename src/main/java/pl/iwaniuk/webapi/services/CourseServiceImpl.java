package pl.iwaniuk.webapi.services;

import com.sun.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.CourseNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.models.Course;
import pl.iwaniuk.webapi.models.Lesson;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.models.edges.Edge_Group_Course;
import pl.iwaniuk.webapi.repository.CourseRepository;
import pl.iwaniuk.webapi.repository.Edge_Group_CourseRepository;
import pl.iwaniuk.webapi.repository.GroupeRepository;
import pl.iwaniuk.webapi.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    GroupeRepository groupeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Edge_Group_CourseRepository edge_group_courseRepository;
    @Autowired
    FileService fileService;

    @Override
    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    @Override
    public Course create(Course course, List<MultipartFile> multipartFiles) throws GroupNotFoundException {
        if (!course.isPublished() && course.getGroupeId() != null) {
            if (!groupeRepository.existsById(course.getGroupeId())) {
                throw new GroupNotFoundException();
            }
            course = courseRepository.save(SaveFilesForCreate(course, multipartFiles));

            edge_group_courseRepository.save(new Edge_Group_Course(groupeRepository.findById(course.getGroupeId()).get(), course));
            return course;
        }
        return courseRepository.save(SaveFilesForCreate(course, multipartFiles));
    }

    @Override
    public Course update(Course courseReq, List<MultipartFile> multipartFiles, String id) throws CourseNotFoundException {

        Course course = getCourseById(id).orElseThrow(CourseNotFoundException::new);

        //region usuwanie plikow z lekcji do usuniecia
        var numberList = courseReq.getLessons().stream().map(l -> l.getNumber()).collect(Collectors.toList());

        var removeLessons = course.getLessons().stream().filter(l -> !(numberList.contains(l.getNumber()))).collect(Collectors.toList());

        for (Lesson item : removeLessons) {
            item.getFile_src().forEach(f -> {
                fileService.deleteFile(f.split("/")[5]);
            });
        }
        //endregion


        for (Lesson lesson : courseReq.getLessons()) {
            String lesson_number = "lesson" + lesson.getNumber();
            var tempLesson = course.getLessons().stream().filter(l -> l.getNumber() == lesson.getNumber()).findFirst();

            if (multipartFiles.stream().anyMatch(m -> m.getOriginalFilename().contains(lesson_number))) {

                if (tempLesson.isPresent()) {

                    tempLesson.get().getFile_src().forEach(fn -> {
                        fileService.deleteFile(fn.split("/")[5]);
                    });
                }

                lesson.setFile_src(SaveFileForUpdateCourse(multipartFiles, lesson_number));

            } else {
                if (tempLesson.isPresent()) {
                    lesson.setFile_src(tempLesson.get().getFile_src());
                }
            }


        }

        courseReq.setPublishedDate(course.getPublishedDate());
        courseReq.setId(course.getId());

        return courseRepository.save(courseReq);
    }

    @Override
    public void delete(Course course) {
        course.getLessons().forEach(l ->
                l.getFile_src().forEach(i ->
                        fileService.deleteFile(
                                i.split("/")[5])
                ));
        if (edge_group_courseRepository.findByCourse_Id(course.getId()).isPresent()) {
            edge_group_courseRepository.delete(edge_group_courseRepository.findByCourse_Id(course.getId()).get());
        }
        courseRepository.delete(course);
    }

    @Override
    public Page<Course> getAllByKindID(String kindID, Pageable page) {
        return courseRepository.findByKind_Id(kindID, page);
    }

    @Override
    public Page<Course> getAllByKindIDAndAuthorID(String kindID, String authorID, Pageable page) {
        return courseRepository.findByKind_IdAndAuthorId(kindID, authorID, page);
    }

    @Override
    public Page<Course> getAllByAuthorID(String authorID, Pageable page) {
        return courseRepository.findByAuthorId(authorID, page);
    }

    @Override
    public Page<Course> getAll(Pageable page) {
        return courseRepository.findAll(page);
    }

    @Override
    public Page<Course> getAllByKindIDAndPublished(boolean b, String kindID, Pageable page) {
        return courseRepository.findByAuthorIdAndPublished(kindID, b, page);
    }

    @Override
    public Page<Course> getAllByKindIDAndAuthorIDAndPublished(boolean b, String kindID, String authorID, Pageable page) {
        return courseRepository.findByKind_IdAndAuthorIdAndPublished(kindID, authorID, b, page);
    }

    @Override
    public Page<Course> getAllByAuthorIDAndPublished(boolean b, String authorID, Pageable page) {
        return courseRepository.findByKind_IdAndPublished(authorID, b, page);
    }

    @Override
    public Page<Course> getAllByPublished(boolean b, Pageable page) {
        return courseRepository.findByPublished(b, page);
    }

    @Override
    public Page<Course> query(String query, PageRequest id) {
        List<Course> result = courseRepository.findByPublishedOrNameIsContainingIgnoreCaseOrKind_NameIsContainingIgnoreCaseOrDecriptionIsContainingIgnoreCase(true,query,query,query);
        List<User> users = userRepository.findByNameContainingIgnoreCaseOrSurenameContainingIgnoreCase(query,query);
        List<Course>result2  = new ArrayList<>();
        users.forEach(u->{
            result2.addAll(courseRepository.findByAuthorIdAndPublished(u.getId(),true));
        });

        result.addAll(result2);
        result = result.stream().distinct().collect(Collectors.toList());

        boolean b = result.removeIf(c -> c.isPublished() == false);
        int start = (int)id.getOffset();
        int end = (start + id.getPageSize()) > result.size() ? result.size() : (start + id.getPageSize());
        return new PageImpl<Course>(result.subList(start,end),id,result.size());
    }


    private List<String> SaveFileForUpdateCourse(List<MultipartFile> multipartFiles, String lessonName) {
        List<String> returnList = new LinkedList<>();
        multipartFiles.forEach(m -> {
            String fileName = m.getOriginalFilename();

            if (fileName.contains(lessonName)) {
                String saveName = fileName.split(",")[1];
                String src = fileService.saveFile(m, saveName);
                src = "http://localhost:8080/api/files/" + src;
                returnList.add(src);
            }

        });

        return returnList;
    }

    private Course SaveFilesForCreate(Course course, List<MultipartFile> multipartFiles) {
        for (Lesson lesson : course.getLessons()) {
            String lesson_number = "lesson" + lesson.getNumber();

            lesson.setFile_src(SaveFileForUpdateCourse(multipartFiles, lesson_number));

        }
        return course;
    }


}
