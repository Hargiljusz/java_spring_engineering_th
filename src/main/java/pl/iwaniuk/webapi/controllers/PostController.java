package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.exceptions.PostNotFoundException;
import pl.iwaniuk.webapi.exceptions.UserNotFoundException;
import pl.iwaniuk.webapi.models.Comment;
import pl.iwaniuk.webapi.models.Post;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.services.CommentService;
import pl.iwaniuk.webapi.services.PostService;
import pl.iwaniuk.webapi.services.UserServices;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserServices userServices;
    @Autowired
    private CommentService commentService;


    @GetMapping("/")
    public Collection<Post> getPosts(@RequestParam(defaultValue = "0") int siteP,
                                     @RequestParam(defaultValue = "10") int sizeP,
                                     @RequestParam(defaultValue = "0") int siteC,
                                     @RequestParam(defaultValue = "10") int sizeC,
                                     @RequestParam(defaultValue = "") String authorId
                                     ){


        if(authorId.equals("")){
           return postService.get(siteP,sizeP,siteC,sizeC,"_id");
        }else{
            return  postService.getByAuthor(authorId,siteP,sizeP,siteC,sizeC,"createDate");
        }

    }
    
    @CrossOrigin
    @GetMapping("/group/{id}")
    public Collection<Post> getPostsByGroup(@RequestParam(defaultValue = "0") int siteP,
                                      @RequestParam(defaultValue = "10") int sizeP,
                                      @RequestParam(defaultValue = "0") int siteC,
                                      @RequestParam(defaultValue = "10") int sizeC,
                                      @PathVariable("id") String groupId){



        return  postService.getByGroup(groupId,siteP,sizeP,siteC,sizeC,"createDate");
    }
    @GetMapping("/me")
    public Collection<Post> getPostsByAuthor(@RequestParam(defaultValue = "0") int siteP,
                                             @RequestParam(defaultValue = "10") int sizeP,
                                             @RequestParam(defaultValue = "0") int siteC,
                                             @RequestParam(defaultValue = "10") int sizeC,
                                       @AuthenticationPrincipal UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws UserNotFoundException {

        User user = userServices.findUserByUserName(usernamePasswordAuthenticationToken.getPrincipal().toString()).orElseThrow(UserNotFoundException::new);

        return  postService.getByAuthor (user.getId(),siteP,sizeP,siteC,sizeC,"createDate");
    }

    @GetMapping("/{id}")
    public Post getPostByID(@PathVariable("id")String id,
                            @RequestParam(defaultValue = "0") int siteC,
                            @RequestParam(defaultValue = "10") int sizeC
    ) throws PostNotFoundException {
        Post post =  postService.getPostById(id,siteC,sizeC).orElseThrow(PostNotFoundException::new);
        //Post post = postService.getPostById(id).orElseThrow(PostNotFoundException::new);
        return  post;
    }

    @PostMapping("/")
    public Post addPost(@Valid @RequestPart("data") Post post,
                        @RequestPart(value = "img", required = false) MultipartFile multipartFile,
                        @RequestPart(value = "files", required = false) MultipartFile [] files) throws PostNotFoundException {
        post.setComments(new ArrayList<>());
        post = postService.create(post,multipartFile,files);
        return post;
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable("id") String id,
                           @Valid @RequestPart("data") Post postReq,
                           @RequestPart(value = "img", required = false) MultipartFile multipartFile,
                           @RequestPart(value = "files", required = false) MultipartFile [] files) throws PostNotFoundException {
        Post post = postService.getPostById(id,0,0).orElseThrow(PostNotFoundException::new);
       /* post.setName(postReq.getName());
        post.setMain_img_src(postReq.getMain_img_src());
        post.setDecription(postReq.getDecription());*/

        return  postService.update(post,postReq,id,multipartFile,files);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePostByID(@PathVariable("id")String id) throws PostNotFoundException {
        Post post = postService.getPostById(id,0,0).orElseThrow(PostNotFoundException::new);
        postService.delete(post);

        return  noContent().build();
    }
}
