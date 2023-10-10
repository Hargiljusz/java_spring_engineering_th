package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.NoAddmisionException;
import pl.iwaniuk.webapi.exceptions.UserNotFoundException;
import pl.iwaniuk.webapi.models.SimpleUser;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.models.auth.UserRegister;
import pl.iwaniuk.webapi.services.UserServices;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/users")
public class UserController {

    final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/")
    public Page<SimpleUser> getAllUsers(
                                    @RequestParam(defaultValue = "0") int site,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "") String role){



        Pageable page = PageRequest.of(site,size,Sort.by("surname").and(Sort.by("name")));

       return userServices.getAll(page).map(user-> new SimpleUser(user.getPhoneNumber(),user.getSurename(),user.getName(),user.getId(),user.getImg_src()));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") String id, @AuthenticationPrincipal UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws UserNotFoundException, NoAddmisionException {
        User user = userServices.findUserById(id).orElseThrow(()->new UserNotFoundException());
        if(usernamePasswordAuthenticationToken.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN"))){

        return  user;
        }
        else {
            throw new NoAddmisionException();
        }
    }
    @GetMapping("/me")
    public User getMe(@AuthenticationPrincipal UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws UserNotFoundException, NoAddmisionException {
        User user = userServices.findUserByUserName(usernamePasswordAuthenticationToken.getPrincipal().toString()).orElseThrow(()->new UserNotFoundException());
         return user;
    }
    @GetMapping("/getSimple/{id}")
    public SimpleUser getSimpleUser(@PathVariable("id") String id) throws UserNotFoundException {
        User user = userServices.findUserById(id).orElseThrow(()->new UserNotFoundException());

        return new SimpleUser(user.getPhoneNumber(),user.getSurename(),user.getName(),user.getId(),user.getImg_src());
    }

    @PutMapping("/{id}")
    public ResponseEntity uspdateUser(
            @PathVariable("id") String id,
            @Valid @RequestPart("data") UserRegister userRegister,
            @RequestPart(value = "img",required = false)MultipartFile multipartFile) throws UserNotFoundException {
        User user = userServices.findUserById(id).orElseThrow(()->new UserNotFoundException());

        userRegister = userServices.update(userRegister,user,multipartFile);

        return ok(userRegister);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") String id) throws UserNotFoundException {
          User user = userServices.findUserById(id).orElseThrow(()->new UserNotFoundException());

          userServices.removeUser(user);

        return noContent().build();
    }

    @GetMapping("/query")
    public Page<SimpleUser> query (@RequestParam(defaultValue = "0") int site,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam() String query){
        var result =  userServices.query(query,PageRequest.of(site,size,Sort.by("surname").and(Sort.by("name"))));
        return result.map(user-> new SimpleUser(user.getPhoneNumber(),user.getSurename(),user.getName(),user.getId(),user.getImg_src()));
    }
}
