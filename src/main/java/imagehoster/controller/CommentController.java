package imagehoster.controller;

import imagehoster.model.Comment;
import imagehoster.model.Image;
import imagehoster.model.User;
import imagehoster.service.CommentService;
import imagehoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    ImageService imageService;


    // Add a comment and link it to an image and a user
    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments",method = RequestMethod.POST)
    public String addComment(Comment comment, @PathVariable("imageId") Integer imageId, HttpSession session)
    {
        User user = (User)session.getAttribute("loggeduser");
        Image image = imageService.getImage(imageId);
        comment.setUser(user);
        comment.setImage(image);
        comment.setCreatedDate(new Date());
        commentService.createComment(comment);
        return "redirect:/images/" + imageId;
    }
}