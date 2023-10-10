package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import pl.iwaniuk.webapi.models.edges.Edge_Group_Course;
import pl.iwaniuk.webapi.models.edges.Member;

import java.util.Collection;

@Document("groups")
public class Group {

    @Id
    private String id;

    private String name;

    private String img_src;

    private Kind kind;

    private String ownerId;

   @JsonIgnore
   @Relations(edges = Member.class,lazy = true,direction = Relations.Direction.INBOUND)
   private Collection<User> members;

    @JsonIgnore
    @Relations(edges = Edge_Group_Course.class,lazy = true,direction = Relations.Direction.OUTBOUND)
    private Collection<Course> courses;


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

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public Kind getKindID() {
        return kind;
    }

    public void setKindID(Kind kind) {
        this.kind = kind;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Collection<User> getMembers() {
        return members;
    }

    public void setMembers(Collection<User> members) {
        this.members = members;
    }

    public Group(String name, String img_src, Kind kind, String ownerId) {
        this.name = name;
        this.img_src = img_src;
        this.kind = kind;
        this.ownerId = ownerId;
    }
}
