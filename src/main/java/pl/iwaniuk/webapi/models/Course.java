package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import pl.iwaniuk.webapi.models.edges.Edge_Group_Course;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Document("courses")
public class Course {
    @Id
    private String id;

    private String name;

    private String decription;

    @Ref
    private Kind kind;

    private String authorId;

    private String groupeId = null;

    private boolean published = true;

    private Collection<Lesson> lessons;

    @CreatedDate
    private Instant publishedDate;

    @JsonIgnore
    @Relations(edges = Edge_Group_Course.class,lazy = true,direction = Relations.Direction.INBOUND)
    private Collection<Group> groups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(String groupeId) {
        this.groupeId = groupeId;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Course(String name, String decription, Kind kind, String authorId, String groupeId, boolean published, Collection<Lesson> lessons) {
        this.name = name;
        this.decription = decription;
        this.kind = kind;
        this.authorId = authorId;
        this.groupeId = groupeId;
        this.published = published;
        this.lessons = lessons;
    }
}
