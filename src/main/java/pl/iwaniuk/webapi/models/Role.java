package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document("roles")
public class Role {

    @Id
    private String id;

    @NotNull
    @NotBlank
    private String role;

    public Role() {
    }

    public Role(@NotNull @NotBlank String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
