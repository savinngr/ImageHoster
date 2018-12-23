package imagehoster.controller;

import imagehoster.model.Image;
import imagehoster.model.User;
import imagehoster.model.UserProfile;
import imagehoster.repository.UserRepository;
import imagehoster.service.ImageService;
import imagehoster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    //This controller method is called when the request pattern is of type 'users/registration'
    @RequestMapping("users/registration")
    public String registration(Model model) {
        User user = new User();
        UserProfile userProfile = new UserProfile();
        user.setProfile(userProfile);

        model.addAttribute("User",user);

        return "users/registration";
    }

    //This controller method is called when the request pattern is of type 'users/registration' and also the incoming request is of POST type
    @RequestMapping(value = "users/registration", method = RequestMethod.POST)
    public String registerUser(User user,Model model) {
        boolean flag = userService.checkPassword(user.getPassword());// To check the strength of the password
        if(flag) {
            userService.registerUser(user);
            return "redirect:/users/login";
        }
        else{
            User newUser = new User();
            UserProfile userProfile = new UserProfile();
            newUser.setProfile(userProfile);
            model.addAttribute("User",newUser);

            String error = "Password must contain atleast 1 alphabet, 1 number & 1 special character";
            model.addAttribute("passwordTypeError",error);

            return "users/registration";
        }
    }

    @RequestMapping("users/login")
    public String login() {
        return "users/login";
    }

    @RequestMapping(value = "users/login", method = RequestMethod.POST)
    public String loginUser(User user, HttpSession session) {
        User loggeduser = userService.login(user);
        if(loggeduser!=null)
        {
            session.setAttribute("loggeduser",loggeduser);
            return "redirect:/images";
        }
        else
        {
            return "redirect:/users/login";
        }
    }

    @RequestMapping(value = "users/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images",images);
        return "index";
    }
}