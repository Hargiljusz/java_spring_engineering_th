package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Ref;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Collection;

@Document("posts")
public class Post {

    @Id
    private String id;

    private String name;

    private String main_img_src;

    private String decription;

    private String authorId;

    private String groupId;

    @CreatedDate
    private Instant createDate;

     private Collection<String> file_src;

    //nie ruszać powoduje nadpisanie customowego zapytania
    //@Ref(lazy = true)
    private Collection<Comment> comments;


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

    public String getMain_img_src() {
        return main_img_src;
    }

    public void setMain_img_src(String main_img_src) {
        this.main_img_src = main_img_src;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public Collection<String> getFile_src() {
        return file_src;
    }

    public void setFile_src(Collection<String> file_src) {
        this.file_src = file_src;
    }

    public Post(String name, String main_img_src, String decription, String authorId, String groupId) {
        this.name = name;
        this.main_img_src = main_img_src;
        this.decription = decription;
        this.authorId = authorId;
        this.groupId = groupId;
    }
}
