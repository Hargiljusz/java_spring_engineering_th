package pl.iwaniuk.webapi.models.auth;

import javax.validation.constraints.*;

public class UserRegister {
    @Email
    private String email;

    private String password;

    @NotBlank
    @NotEmpty
    @Size(min = 9,max = 9)
    @Pattern(message = "quantity must be a number", regexp="^[0-9]*$")
    private String phoneNumber;

   // private boolean special = false;

    @NotBlank
    @NotEmpty
    @Size(max = 35)
    private String name;

    @NotBlank
    @NotEmpty
    @Size(max =35)
    private String surename;

    private String img_src;

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

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
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

   /* public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }*/
}
