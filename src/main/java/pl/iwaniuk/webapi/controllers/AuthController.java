package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.UserWithEmailExistException;
import pl.iwaniuk.webapi.models.Role;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.models.auth.SpecialUserRegister;
import pl.iwaniuk.webapi.models.auth.UserCredentials;
import pl.iwaniuk.webapi.models.auth.UserRegister;
import pl.iwaniuk.webapi.services.FileService;
import pl.iwaniuk.webapi.services.RoleService;
import pl.iwaniuk.webapi.services.UserServices;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class AuthController {

    @Autowired
    UserServices userServices;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    FileService fileService;


    @PostMapping("/login")
    public void login(@RequestBody UserCredentials userCredentials){

    }

    @PostMapping(value = "/register")
    public ResponseEntity register(
            @RequestPart("data") @Valid UserRegister userRegister,
            @RequestPart("img")MultipartFile multipartFile,
            @RequestParam(defaultValue = "false") boolean isEducator) throws UserWithEmailExistException {

            if(userServices.isExistByUsername(userRegister.getEmail())){
                throw new UserWithEmailExistException();
            }

            String src = fileService.saveFile(multipartFile);
        src = "http://localhost:8080/api/files/"+src;
        List<Role> roles = new ArrayList<>();
                Role roleUser = roleService.findRoleByName("ROLE_USER");
                roles.add(roleUser);
                if(isEducator){
                    Role role_educator = roleService.findRoleByName("ROLE_EDUCATOR");
                    roles.add(role_educator);
                }
                User user = new User(
                        userRegister.getEmail()
                        ,passwordEncoder.encode(userRegister.getPassword())
                        , userRegister.getPhoneNumber(), true);
                user.setRoles(roles);
                user.setImg_src(src);
                user.setName(userRegister.getName());
                user.setSurename(userRegister.getSurename());
           User saveU = userServices.saveUser(user);

        Map<Object, Object> model = new HashMap<>();
        model.put("msg","succes");

            return  ok(model);
    }

    @PostMapping("/registerSpecialUser")
    public ResponseEntity registerSpecialUser(@RequestBody @Valid SpecialUserRegister userRegister) throws UserWithEmailExistException {

        if(userServices.isExistByUsername(userRegister.getEmail())){
            throw new UserWithEmailExistException();
        }
        List<Role> roles = new ArrayList<>();

        roles.add(roleService.findRoleByName("ROLE_ADMIN"));

         if(userRegister.isEducator())
             roles.add(roleService.findRoleByName("ROLE_EDUCATOR"));

        if(userRegister.isUser())
            roles.add(roleService.findRoleByName("ROLE_USER"));

        User user = new User(
                userRegister.getEmail()
                ,passwordEncoder.encode(userRegister.getPassword())
                , userRegister.getPhoneNumber(), true);
        user.setRoles(roles);
        User saveU = userServices.saveUser(user);

        Map<Object, Object> model = new HashMap<>();
        model.put("msg","succes");

        return  ok(model);
    }
}
