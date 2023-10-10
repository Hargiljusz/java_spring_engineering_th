package pl.iwaniuk.webapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.models.auth.UserRegister;

import java.util.Optional;

public interface UserServices extends UserDetailsService {
    boolean isExistByUsername(String username);
    User saveUser(User user);
    Page<User> getAll(Pageable pageable);
    Page<User> getAllByConfirm(Pageable pageable,boolean confirm);
    Optional<User> findUserById(String id);
    Optional<User> findUserByUserName(String username);
    void removeUser(User user);

     Page<User>query (String query, PageRequest id);

    UserRegister update(UserRegister userRegister, User user, MultipartFile multipartFile);
}
