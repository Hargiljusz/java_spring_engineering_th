package pl.iwaniuk.webapi.repository;

import com.arangodb.springframework.annotation.BindVars;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.iwaniuk.webapi.models.Post;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PostRepository extends ArangoRepository<Post,String> {

    @Query("FOR p in @@col " +
            "SORT p.@sortName DESC " +
                "LIMIT @offsetPost , @countPost " +
                    "LET temp = ( " +
                    "FOR c IN (DOCUMENT(@comDoc,p.comments)) " +
                    "SORT c.createdTime DESC " +
                    "LIMIT @offsetComments , @countComments " +
                    "return c " +
                ")" +
            "return  MERGE(p,{comments: temp})"
    )
    Collection<Post> getAll(@BindVars Map<String, Object> bindvars);

    @Query("FOR post in posts FILTER post._key == @postID " +
            "UPDATE post WITH {comments: APPEND(post.comments,@comID)} IN posts ")
    void updateCommentsInPost(@Param("postID") String postID,@Param("comID") String comID);

    boolean existsByAuthorIdAndGroupId(String authorID,String GroupID);

    @Query("FOR post in posts FILTER POSITION(post.comments,@comID) == true " +
            "UPDATE post WITH {comments: REMOVE_VALUE(post.comments,@comID)} IN posts ")
    void deleteCommentsInPost(@Param("comID") String comID);

    @Query("FOR p in @@col " +
            "FILTER p.authorId == @authorID "+
            "SORT p.@sortName DESC " +
                "LIMIT @offsetPost , @countPost " +
                    "LET temp = ( " +
                    "FOR c IN (DOCUMENT(@comDoc,p.comments)) " +
                    "SORT c.createdTime DESC " +
                    "LIMIT @offsetComments , @countComments " +
                    "return c " +
                ") " +
            "return  MERGE(p,{comments: temp})"
    )
    Collection<Post> getAllByAuthor(@BindVars Map<String, Object> bindvars);

    @Query("FOR p in @@col " +
            "FILTER p.groupId == @groupID "+
            "SORT p.@sortName DESC " +
                "LIMIT @offsetPost , @countPost " +
                    "LET temp = ( " +
                    "FOR c IN (DOCUMENT(@comDoc,p.comments)) " +
                 "SORT c.createdTime DESC " +
                 "LIMIT @offsetComments , @countComments " +
                 "return c " +
                ") " +
            "return  MERGE(p,{comments: temp})"
    )
    Collection<Post> getAllByGroup(@BindVars Map<String, Object> bindvars);

    @Query("FOR p in @@col " +
            "FILTER p._key == @postID "+
                "LET temp = ( " +
                "FOR c IN (DOCUMENT(@comDoc,p.comments)) " +
                 "SORT c.createdTime DESC " +
                "LIMIT @offsetComments , @countComments " +
                "return c " +
            ")" +
            "return  MERGE(p,{comments: temp})"
    )
    Optional<Post> getOneByID(@BindVars Map<String, Object> bindvars);

    @Query("FOR p in posts " +
            "FILTER p._key == @postID "+
            "UPDATE p WITH {name: @name, main_img_src: @img, decription: @desc,file_src:@files_src} IN posts"
    )
    void updateOneByID(@BindVars Map<String, Object> bindvars);

}
