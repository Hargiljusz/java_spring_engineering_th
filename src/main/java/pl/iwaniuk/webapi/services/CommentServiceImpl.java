package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iwaniuk.webapi.exceptions.PostNotFoundException;
import pl.iwaniuk.webapi.models.Comment;
import pl.iwaniuk.webapi.models.Group;
import pl.iwaniuk.webapi.models.Post;
import pl.iwaniuk.webapi.models.User;
import pl.iwaniuk.webapi.repository.CommentRepository;
import pl.iwaniuk.webapi.repository.PostRepository;
import pl.iwaniuk.webapi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<Comment> getByID(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment create(Comment comment, String id) throws PostNotFoundException {
        if(!postRepository.existsById(id)){
            throw  new PostNotFoundException();
        }

        //zapisanie kometarza
        comment = commentRepository.save(comment);

        postRepository.updateCommentsInPost(id,"comments/"+comment.getId());
        return  comment;
    }

    @Override
    public Comment update(Comment comment, Comment commentReq) {
        comment.setConntent(commentReq.getConntent());
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment){

        commentRepository.delete(comment);
        postRepository.deleteCommentsInPost("comments/"+comment.getId());
    }

    @Override
    public Page<Comment> get(Pageable page) {
        return commentRepository.findAll(page);
    }

    @Override
    public Page<Comment> query(String query, PageRequest page) {
        List<Comment> result = commentRepository.findByConntentIsContainingIgnoreCase(query);
        List<User> users = userRepository.findByNameContainingIgnoreCaseOrSurenameContainingIgnoreCase(query,query);
        List<Comment>result2  = new ArrayList<>();
        users.forEach(u->{
            result2.addAll(commentRepository.findByAuthorId(u.getId()));
        });

        result.addAll(result2);
        result.addAll(commentRepository.findByAuthorId(query));
        result = result.stream().distinct().collect(Collectors.toList());

        int start = (int)page.getOffset();
        int end = (start + page.getPageSize()) > result.size() ? result.size() : (start + page.getPageSize());
        return new PageImpl<Comment>(result.subList(start,end),page,result.size());
    }
}
