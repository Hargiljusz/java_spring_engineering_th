package pl.iwaniuk.webapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iwaniuk.webapi.exceptions.CommentNotFoundException;
import pl.iwaniuk.webapi.exceptions.GroupNotFoundException;
import pl.iwaniuk.webapi.exceptions.PostNotFoundException;
import pl.iwaniuk.webapi.models.Comment;
import pl.iwaniuk.webapi.services.CommentService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/")
    public Page<Comment> getAll(@RequestParam(defaultValue = "0") int site,
                                @RequestParam(defaultValue = "10") int size){
        Pageable page = PageRequest.of(site,size);
        return commentService.get(page);
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable("id")String id) throws  CommentNotFoundException {
        return commentService.getByID(id).orElseThrow(CommentNotFoundException::new);
    }


    @PostMapping("/{postId}")
    public Comment create(@PathVariable("postId")String id,@Valid @RequestBody Comment comment) throws PostNotFoundException {
        return commentService.create(comment,id);
    }

    @PutMapping("/{id}")
    public Comment updateById(@PathVariable("id")String id,@Valid @RequestBody Comment commentReq) throws CommentNotFoundException {
        Comment comment =  commentService.getByID(id).orElseThrow(CommentNotFoundException::new);
        return commentService.update(comment,commentReq);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id")String id) throws CommentNotFoundException {
         Comment comment = commentService.getByID(id).orElseThrow(CommentNotFoundException::new);
         commentService.delete(comment);
         return noContent().build();
    }

    @GetMapping("/query")
    public Page<Comment> query( @RequestParam() String query,
                                @RequestParam(defaultValue = "0") int site,
                                @RequestParam(defaultValue = "10") int size){
        return commentService.query(query,PageRequest.of(site,size));
    }
}
