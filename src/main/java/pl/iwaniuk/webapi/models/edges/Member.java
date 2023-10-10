package pl.iwaniuk.webapi.models.edges;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.User;

import java.time.Instant;

@Edge
public class Member {
    @Id
    private String id;

    @From
    private User user;

    @To
    private Group group;

    private boolean isAccept;

    @CreatedDate
    private Instant sendInvite;

    public Member(User user, Group group, boolean isAccept) {
        this.user = user;
        this.group = group;
        this.isAccept = isAccept;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean confirm) {
        isAccept = confirm;
    }

    public Instant getSendInvite() {
        return sendInvite;
    }

    public void setSendInvite(Instant sendInvite) {
        this.sendInvite = sendInvite;
    }
}
