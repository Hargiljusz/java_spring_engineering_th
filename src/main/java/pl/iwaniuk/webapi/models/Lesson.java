package pl.iwaniuk.webapi.models;

import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

import java.util.Collection;

@Document("lessons")
public class Lesson {
    //this is id for lessons, generete in C# app
    private int number;

    private String subject;

    private String conntent;

    private String video_link = null;

    private Collection<String> file_src;

    private String homework;

    public Lesson(int number, String subject, String conntent, String video_link, Collection<String> file_src, String homework) {
        this.number = number;
        this.subject = subject;
        this.conntent = conntent;
        this.video_link = video_link;
        this.file_src = file_src;
        this.homework = homework;
    }

    public Lesson() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getConntent() {
        return conntent;
    }

    public void setConntent(String conntent) {
        this.conntent = conntent;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public Collection<String> getFile_src() {
        return file_src;
    }

    public void setFile_src(Collection<String> file_src) {
        this.file_src = file_src;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
