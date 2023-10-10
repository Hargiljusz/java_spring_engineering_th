package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.models.auth.UserRegister;
import pl.iwaniuk.webapi.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    FileService fileService;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userFromA_DB = userRepository.findByEmail(s);
        pl.iwaniuk.webapi.models.User user;
        if(userFromA_DB.isPresent()){
            user = userFromA_DB.get();
        }else{
            throw new UsernameNotFoundException(s);
        }
        return wrap_User_To_UserDetails(user);
    }

    private UserDetails wrap_User_To_UserDetails(pl.iwaniuk.webapi.models.User user) {
        Set<GrantedAuthority> grantedAuthorities =
                user
                        .getRoles()
                        .stream()
                        .map(r->new SimpleGrantedAuthority(r.getRole()))
                        .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getEmail()
                ,user.getPassword()
                ,user.isConfim()
                ,true
                ,true
                ,true
                ,grantedAuthorities);
    }

    @Override
    public boolean isExistByUsername(String username) {
        return userRepository.existsByEmail(username);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getAllByConfirm(Pageable pageable, boolean confirm) {
        return  userRepository.findByConfim(pageable,confirm);
    }

    @Override
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public void removeUser(User user) {
        if(user.getImg_src()!=null){
            fileService.deleteFile(user.getImg_src().split("/")[5]);
        }
        userRepository.delete(user);
    }

    @Override
    public Page<User> query(String query, PageRequest id) {
        return userRepository.findByNameContainingIgnoreCaseOrSurenameContainingIgnoreCaseOrIdOrEmail(query,query,query,query,id);
    }

    @Override
    public UserRegister update(UserRegister userRegister, User user, MultipartFile multipartFile) {
        if(multipartFile!=null){
            if(user.getImg_src()!=null){
                fileService.deleteFile(user.getImg_src().split("/")[5]);
            }
            String src = fileService.saveFile(multipartFile);
            src = "http://localhost:8080/api/files/"+src;
            user.setImg_src(src);
        }

        user.setName(userRegister.getName());
        user.setSurename(userRegister.getSurename());
        user.setPhoneNumber(userRegister.getPhoneNumber());
        userRepository.save(user);
        return userRegister;
    }
}
