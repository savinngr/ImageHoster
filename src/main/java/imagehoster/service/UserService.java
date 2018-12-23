package imagehoster.service;

import imagehoster.model.User;
import imagehoster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    //We are not currently storing the details of the user anywhere
    //We will be storing the user details in the Database & ORMs part
    public void registerUser(User newUser) {
        repository.registerUser(newUser);
    }

    public User login(User user)
    {
        User existingUser = repository.checkUser(user);
        if (existingUser!=null)
            return existingUser;
        else
            return null;
    }

    public boolean checkPassword(String password)
    {
        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern specialCharacter = Pattern.compile("[^0-9a-zA-Z]");

        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecialCharacter = specialCharacter.matcher(password);

        return (hasDigit.find()&&hasLetter.find()&&hasSpecialCharacter.find());
    }

}
