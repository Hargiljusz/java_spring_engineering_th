package pl.iwaniuk.webapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.iwaniuk.webapi.exceptions.PostNotFoundException;
import pl.iwaniuk.webapi.models.Comment;

import java.util.Optional;

public interface CommentService {
    Optional<Comment> getByID(String id);

    Comment create(Comment comment, String id) throws PostNotFoundException;

    Comment update(Comment comment,Comment commentReq);

    void delete(Comment comment);

    Page<Comment> get(Pageable page);

    Page<Comment> query(String query, PageRequest page);
}
