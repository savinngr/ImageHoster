package imagehoster.repository;

import imagehoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Repository
public class UserRepository {

    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public void registerUser(User newUser){
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(newUser);
            transaction.commit();
        }catch (Exception e)
        {
            transaction.rollback();
        }
    }

    public User checkUser(User user)
    {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> typedQuery = em.createQuery("select p from User p where p.username = :username and p.password = :password",User.class);
        typedQuery.setParameter("username",user.getUsername());
        typedQuery.setParameter("password",user.getPassword());
        try {
            return typedQuery.getSingleResult();
        }catch(NoResultException e)
        {
            return null;
        }
    }

}
