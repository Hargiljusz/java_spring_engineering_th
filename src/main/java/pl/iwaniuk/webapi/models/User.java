package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.HashIndex;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import pl.iwaniuk.webapi.models.edges.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;

@Document("users")
@HashIndex(fields = { "email" }, unique = true)
public class User {

    @Id
    private String id;

    @Email
    private String email;

    private String password;

    @NotBlank
    @NotEmpty
    @Size(min = 9,max = 9)
    private String phoneNumber;

    private boolean confim = false;

    @NotBlank
    @NotEmpty
    @Size(max = 35)
    private String name;

    @NotBlank
    @NotEmpty
    @Size(max =35)
    private String surename;

    private String img_src;

    @CreatedDate
    private Instant ceateDate;

    @Ref
    private Collection<pl.iwaniuk.webapi.models.Role>roles;

    @JsonIgnore
    @Relations(edges = Member.class,lazy = true,direction = Relations.Direction.OUTBOUND)
    private Collection<Group> groups;

    public User() {
    }

    public User(@Email String email, String password, @NotBlank @NotEmpty @Size(min = 9, max = 9) String phoneNumber, boolean confim) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.confim = confim;
    }

    public User(@Email String email, String password, @NotBlank @NotEmpty @Size(min = 9, max = 9) String phoneNumber, boolean confim, @NotBlank @NotEmpty @Size(max = 35) String name, @NotBlank @NotEmpty @Size(max = 35) String surename) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.confim = confim;
        this.name = name;
        this.surename = surename;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isConfim() {
        return confim;
    }

    public void setConfim(boolean confim) {
        this.confim = confim;
    }

    public Collection<pl.iwaniuk.webapi.models.Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<pl.iwaniuk.webapi.models.Role> roles) {
        this.roles = roles;
    }

    public Instant getCeateDate() {
        return ceateDate;
    }

    public void setCeateDate(Instant ceateDate) {
        this.ceateDate = ceateDate;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }
}
