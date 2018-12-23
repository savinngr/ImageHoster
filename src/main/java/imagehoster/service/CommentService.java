package imagehoster.service;

import imagehoster.model.Comment;
import imagehoster.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentRepository repository;

    public void createComment(Comment comment){
        repository.createComment(comment);
    }

}
