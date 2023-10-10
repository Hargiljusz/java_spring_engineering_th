package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


@Document("comments")
public class Comment {
    @Id
    private String id;

    private String authorId;

    private String conntent;

    @CreatedDate
    private Instant createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getConntent() {
        return conntent;
    }

    public void setConntent(String conntent) {
        this.conntent = conntent;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Comment(String authorId, String conntent) {
        this.authorId = authorId;
        this.conntent = conntent;
    }

    public Comment() {
    }
}
