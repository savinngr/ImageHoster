package imagehoster.controller;

import imagehoster.model.Comment;
import imagehoster.model.Image;
import imagehoster.model.Tag;
import imagehoster.model.User;
import imagehoster.service.ImageService;
import imagehoster.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;


    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }


    @RequestMapping("/images/{id}")
    public String showImage(@PathVariable("id") Integer id, Model model) {
        Image image = imageService.getImage(id);
        model.addAttribute("image", image);
        model.addAttribute("tags",image.getTags());
        model.addAttribute("comments",image.getComments());
        return "images/image";
    }


    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }

    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags, Image newImage, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loggeduser");
        newImage.setUser(user);
        String uploadedImageData = convertUploadedFileToBase64(file);
        newImage.setImageFile(uploadedImageData);

        List<Tag> imageTags = findOrCreateTags(tags);
        newImage.setTags(imageTags);
        newImage.setDate(new Date());
        imageService.uploadImage(newImage);
        return "redirect:/images";
    }

    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model) {
        Image image = imageService.getImage(imageId);
        String tags = convertTagsToString(image.getTags());
        model.addAttribute("image", image);
        model.addAttribute("tags",tags);
        return "images/edit";
    }


    @RequestMapping(value = "/editImage", method = RequestMethod.PUT)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId,@RequestParam("tags") String tags, Image updatedImage, HttpSession session) throws IOException {

        Image image = imageService.getImage(imageId);
        String updatedImageData = convertUploadedFileToBase64(file);
        List<Tag> imageTags = findOrCreateTags(tags);

        if (updatedImageData.isEmpty())
            updatedImage.setImageFile(image.getImageFile());
        else {
            updatedImage.setImageFile(updatedImageData);
        }

        updatedImage.setId(imageId);
        User user = (User) session.getAttribute("loggeduser");
        updatedImage.setUser(user);
        updatedImage.setTags(imageTags);
        updatedImage.setDate(new Date());

        imageService.updateImage(updatedImage);
        return "redirect:/images/" + updatedImage.getTitle();
    }

    //This controller method is called when the request pattern is of type 'deleteImage' and also the incoming request is of DELETE type
    //The method calls the deleteImage() method in the business logic passing the id of the image to be deleted
    //Looks for a controller method with request mapping of type '/images'
    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, HttpSession session,Model model) {
        User user = (User) session.getAttribute("loggeduser");
        boolean flag = imageService.checkUser(user,imageId);
        if(flag) {
            imageService.deleteImage(imageId);
            return "redirect:/images";
        }
        else{
            Image image = imageService.getImage(imageId);
            model.addAttribute("image", image);
            model.addAttribute("tags",image.getTags());
            model.addAttribute("editError","Only the owner of the image can delete the image");
            return "images/image";
        }
    }

    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();
            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }

    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }

        Tag lastTag = tags.get(tags.size() - 1);
        tagString.append(lastTag.getName());

        return tagString.toString();
    }
}
