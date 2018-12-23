package imagehoster.repository;

import imagehoster.model.Image;
import imagehoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ImageRespository {

    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public void uploadImage(Image image)
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(image);
            transaction.commit();
        }catch (Exception e)
        {
            transaction.rollback();
        }
    }

    public List<Image> getAllImages()
    {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = em.createQuery("select p from Image p",Image.class);
        List<Image> images = typedQuery.getResultList();
        return images;
    }

    public Image getImageByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = em.createQuery("select p from Image p where p.title=:title",Image.class);
        typedQuery.setParameter("title",title);
        Image image;
        try {
            image = typedQuery.getSingleResult();
        }catch(NoResultException e)
        {
            return null;
        }
        return image;
    }

    public Image getImage(Integer imageId)
    {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = em.createQuery("select p from Image p where p.id=:id",Image.class);
        typedQuery.setParameter("id",imageId);
        Image image;
        try{
            image = typedQuery.getSingleResult();
        }catch (NoResultException e)
        {
            return null;
        }
        return  image;
    }

    public void updateImage(Image updatedImage)
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(updatedImage);
            transaction.commit();
        }catch (Exception e)
        {
            transaction.rollback();
        }
    }

    public void deleteImage(Integer imageId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Image image = em.find(Image.class,imageId);
            em.remove(image);
            transaction.commit();
        }catch (Exception e)
        {
            transaction.rollback();
        }
    }

    public boolean checkUser(User user, Integer imageId)
    {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = em.createQuery("select p from Image p where p.id=:imageId and p.user=:userId", Image.class);
        typedQuery.setParameter("imageId",imageId);
        typedQuery.setParameter("userId",user);
        try{
            Image image = typedQuery.getSingleResult();
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }
}

