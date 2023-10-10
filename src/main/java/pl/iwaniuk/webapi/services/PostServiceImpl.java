package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.iwaniuk.webapi.models.Post;
import pl.iwaniuk.webapi.repository.PostRepository;

import javax.validation.Valid;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    PostRepository postRepository;
    @Autowired
    FileService fileService;

    @Override
    public Collection<Post> getByGroup(String groupId,int siteP, int sizeP, int siteC, int sizeC, String sort) {
        Map<String, Object> bindvars = new HashMap<>();
        bindvars.put("offsetPost",(siteP*sizeP));
        bindvars.put("countPost",sizeP);
        bindvars.put("offsetComments",(siteC*sizeC));
        bindvars.put("countComments",(siteC*sizeC)+sizeC);
        bindvars.put("sortName",sort);
        bindvars.put("groupID",groupId);
        bindvars.put("comDoc","comments");
        bindvars.put("@col","posts");

        return postRepository.getAllByGroup(bindvars);
    }

    @Override
    public Collection<Post> getByAuthor(String authorId, int siteP, int sizeP, int siteC, int sizeC, String sort) {

        Map<String, Object> bindvars = new HashMap<>();
        bindvars.put("offsetPost",(siteP*sizeP));
        bindvars.put("countPost",sizeP);
        bindvars.put("offsetComments",(siteC*sizeC));
        bindvars.put("countComments",(siteC*sizeC)+sizeC);
        bindvars.put("sortName",sort);
        bindvars.put("authorID",authorId);
        bindvars.put("comDoc","comments");
        bindvars.put("@col","posts");

        return postRepository.getAllByAuthor(bindvars);
    }

    @Override
    public Collection<Post> get(int siteP, int sizeP, int siteC, int sizeC, String sort) {

        Map<String, Object> bindvars = new HashMap<>();
        bindvars.put("offsetPost",(siteP*sizeP));
        bindvars.put("countPost",sizeP);
        bindvars.put("offsetComments",(siteC*sizeC));
        bindvars.put("countComments",(siteC*sizeC)+sizeC);
        bindvars.put("sortName",sort);
        bindvars.put("comDoc","comments");
        bindvars.put("@col","posts");

        return postRepository.getAll(bindvars);
    }


    @Override
    public Optional<Post> getPostById(String id,int siteC, int sizeC) {

        Map<String, Object> bindvars = new HashMap<>();
        bindvars.put("offsetComments",(siteC*sizeC));
        bindvars.put("countComments",sizeC);
        bindvars.put("comDoc","comments");
        bindvars.put("postID",id);
        bindvars.put("@col","posts");

        return postRepository.getOneByID(bindvars);
    }

    @Override
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    @Override
    public Post create(Post post, MultipartFile multipartFile, MultipartFile[] files) {
        if(multipartFile != null){
        String src = fileService.saveFile(multipartFile);
        src = "http://localhost:8080/api/files/" + src;
        post.setMain_img_src(src);
        }else{
            post.setMain_img_src(null);
        }
        if(files!=null){
            saveFile(post, files);
        }else{
            post.setFile_src(null);
        }
        return postRepository.save(post);
    }

    private void saveFile(Post post, MultipartFile[] files) {
        List<String> listOfLinks = new ArrayList<>(files.length);
        for (MultipartFile file:files) {
            String src = fileService.saveFile(file);
            src = "http://localhost:8080/api/files/" + src;
            listOfLinks.add(src);
        }
        post.setFile_src(listOfLinks);
    }

    @Override
    public Post update(Post post, @Valid Post postReq, String id, MultipartFile multipartFile, MultipartFile[] files) {

        if(multipartFile != null){
            if(post.getMain_img_src() !=null) {
                fileService.deleteFile(post.getMain_img_src().split("/")[5]);
            }

            String src = fileService.saveFile(multipartFile);
            src = "http://localhost:8080/api/files/" + src;
            post.setMain_img_src(src);
        }
        if(files!=null){
            if(post.getFile_src() !=null) {
                for (String file_src : post.getFile_src()) {
                    fileService.deleteFile(file_src.split("/")[5]);
                }
            }
            saveFile(post, files);
        }else{
            post.setFile_src(null);
        }
        post.setName(postReq.getName());
        post.setDecription(postReq.getDecription());

        Map<String, Object> bindvars = new HashMap<>();
        bindvars.put("postID",id);
        bindvars.put("name",post.getName());
        bindvars.put("img",post.getMain_img_src());
        bindvars.put("desc",post.getDecription());
        bindvars.put("files_src",post.getFile_src());

       postRepository.updateOneByID(bindvars);

        Map<String, Object> bindvars2 = new HashMap<>();
        bindvars2.put("offsetComments",0);
        bindvars2.put("countComments",0);
        bindvars2.put("comDoc","comments");
        bindvars2.put("postID",id);
        bindvars2.put("@col","posts");

       return  postRepository.getOneByID(bindvars2).get();

    }

    private String toAQLArray(Collection<String> file_src) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        return  null;
    }

    @Override
    public void delete(Post post) {
        if(post.getMain_img_src()!=null) {
            fileService.deleteFile(post.getMain_img_src().split("/")[5]);
        }
        postRepository.delete(post);
    }
}
