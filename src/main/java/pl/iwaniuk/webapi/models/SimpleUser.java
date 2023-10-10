package pl.iwaniuk.webapi.models;

public class SimpleUser {
    private String phoneNumber;
    private String surname;
    private String name;
    private String id;
    private String img_src;

    public SimpleUser(String phoneNumber, String surname, String name, String id, String img_src) {
        this.phoneNumber = phoneNumber;
        this.surname = surname;
        this.name = name;
        this.id = id;
        this.img_src = img_src;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }
}
