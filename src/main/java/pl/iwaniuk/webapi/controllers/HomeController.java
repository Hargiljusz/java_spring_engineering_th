package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pl.iwaniuk.webapi.repository.CourseRepository;
import pl.iwaniuk.webapi.repository.UserRepository;



@RestController
public class HomeController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/api/hello")
    public String hello() {
        return "hello ";
               // +courseRepository.findByNameIsContainingIgnoreCaseOrKind_NameIsContainingIgnoreCaseOrDecriptionIsContainingIgnoreCaseAndPublished("", "KA","",true).size();
    }


    @GetMapping("/api/hello2")
    public String hello2() {
        return "secret hello must be auth";
    }



}
