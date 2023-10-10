package pl.iwaniuk.webapi.services;

import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.models.Post;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

public interface PostService {
    Collection<Post> getByGroup(String groupId, int siteP, int sizeP, int siteC, int sizeC, String sort);

    Collection<Post> getByAuthor(String authorId, int siteP, int sizeP, int siteC, int sizeC, String sort);

    Collection<Post> get(int siteP, int sizeP, int siteC, int sizeC, String sort);

    Optional<Post> getPostById(String id,int siteC, int sizeC);

    Optional<Post> getPostById(String id);

    Post create(Post post, MultipartFile multipartFile, MultipartFile[] files);

    Post update(Post post, @Valid Post postReq, String id, MultipartFile multipartFile, MultipartFile[] files);

    void delete(Post post);
}
