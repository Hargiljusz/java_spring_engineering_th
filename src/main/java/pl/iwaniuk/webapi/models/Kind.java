package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

@Document("kinds")
public class Kind {

    @Id
    private String id;

    private String name;

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

    public Kind(String name) {
        this.name = name;
    }

    public Kind() {
    }
}
