package pl.iwaniuk.webapi.config;

import com.arangodb.springframework.core.ArangoOperations;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.iwaniuk.webapi.models.*;
import pl.iwaniuk.webapi.models.edges.Member;
import pl.iwaniuk.webapi.repository.*;
import pl.iwaniuk.webapi.services.CommentService;
import pl.iwaniuk.webapi.services.PostService;
import pl.iwaniuk.webapi.services.UserServices;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
public class RepositoriesInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ArangoOperations arangoOperations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    KindRepository kindRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    GroupeRepository groupeRepository;


    @Bean
    InitializingBean init() {
        return () ->{
            if(userRepository.count()==0){
            //arangoOperations.dropDatabase();

           Role admin = roleRepository.save(new Role("ROLE_ADMIN"));
           Role user = roleRepository.save(new Role("ROLE_USER"));

            User userA = new User("jakub@gamil.com",passwordEncoder.encode("test"),"111222333",false);
            userA.setSurename("Iwaniuk");
            userA.setName("Jakub");
            userA.setRoles(new HashSet<>(Arrays.asList(admin)));

            User userU = new User("test@gamil.com",passwordEncoder.encode("test"),"111222333",false);
            userU.setRoles(new HashSet<>(Arrays.asList(user)));

            userRepository.saveAll(Arrays.asList(userA,userU));
        }

           /* if(postRepository.count()==0){
              Post post1 = new Post("Test 1","dummy1.png","description","484542","0");
              Post post2 = new Post("Test 2","dummy2.png","description","71681","1");

               post1.setComments(new ArrayList<Comment>());
               post2.setComments(new ArrayList<Comment>());

              List<Comment> temp1 = Arrays.asList(new Comment("71681","test"),new Comment("71681","test2"),new Comment("71681","test3"));
              List<Comment> temp2 = Arrays.asList(new Comment(),new Comment("71681","test4"),new Comment("71681","test5"),new Comment("71681","test6"),new Comment("71681","test7"),new Comment("71681","test8"));

              temp1 = StreamSupport.stream(commentRepository.saveAll(temp1).spliterator(),false).collect(Collectors.toList());
              temp2 = StreamSupport.stream(commentRepository.saveAll(temp2).spliterator(),false).collect(Collectors.toList());

              List<Post> postList = StreamSupport.stream(postRepository.saveAll(Arrays.asList(post1,post2)).spliterator(),false).collect(Collectors.toList());

            temp1.forEach(comment -> postRepository.updateCommentsInPost(postList.get(0).getId(),"comments/"+ comment.getId()));
                temp2.forEach(comment -> postRepository.updateCommentsInPost(postList.get(1).getId(),"comments/"+ comment.getId()));

          }*/

            if(kindRepository.count()==0 && courseRepository.count()==0){
                Iterable<Kind> temp =  kindRepository.saveAll(
                        Arrays.asList(
                                new Kind("Fizyka"),
                                new Kind("Polski"),
                                new Kind("Informatyka"),
                                new Kind("Matematyka"))
                );
                List<Kind> result =
                        StreamSupport.stream(temp.spliterator(), false)
                                .collect(Collectors.toList());

               Course course1 = new Course("Test 1"," description",result.get(2),"0","0",true,
                       Arrays.asList(
                               new Lesson(),
                               new Lesson(1,"Subject123","testcontent",null,null,"homework")
                       ));


               Course course2 = new Course("Test 2"," description",result.get(1),"0","0",false,null);

               courseRepository.saveAll(Arrays.asList(course1,course2));
            }

            /*if(memberRepository.count()==0 && groupeRepository.count() ==0){
              Group group =  groupeRepository.save(new Group("Fizyku u Barbary","princess.jpg",kindRepository.findById("498262").get(),"123"));
               Member member =  memberRepository.save(new Member(userRepository.findById("71681").get(),group,false));
            }*/
        };
    }
}
