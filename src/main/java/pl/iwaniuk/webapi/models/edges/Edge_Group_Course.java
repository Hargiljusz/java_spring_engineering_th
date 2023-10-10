package pl.iwaniuk.webapi.models.edges;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;
import pl.iwaniuk.webapi.models.Course;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.User;

@Edge
public class Edge_Group_Course {

    @Id
    private String id;

    @From
    private Group group;

    @To
    private Course course;

    public Edge_Group_Course(Group group, Course course) {
        this.group = group;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
