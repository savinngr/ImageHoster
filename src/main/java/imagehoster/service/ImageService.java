package imagehoster.service;

import imagehoster.model.Image;
import imagehoster.model.User;
import imagehoster.repository.ImageRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRespository repository;


    public List<Image> getAllImages() {
        return repository.getAllImages();
    }

    public void uploadImage(Image image) {
        repository.uploadImage(image);
    }

    public Image getImageByTitle(String title) {
        return repository.getImageByTitle(title);
    }

    public Image getImage(Integer imageId)
    {
        return repository.getImage(imageId);
    }

    public void updateImage(Image updatedImage)
    {
        repository.updateImage(updatedImage);
    }

    public void deleteImage(Integer imageId)
    {
        repository.deleteImage(imageId);
    }

    public boolean checkUser(User user, Integer imageId)
    {
        if(repository.checkUser(user,imageId))
        {
            return true;
        }

        return false;
    }

}
