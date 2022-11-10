package facades;

import dtos.UserDTO;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("select u from User u where u.userName= :username", User.class);
            query.setParameter("username", username);
            user = query.getSingleResult();
            //old method when userName was id:
//            user = em.find(User.class, username);
            //todo: maybe throw different error messages depending on if password or username is wrong
            if (/*user == null ||*/ !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        }catch (NoResultException e) {
            throw new AuthenticationException("Invalid user name or password");
        } finally {
            em.close();
        }
        return user;
    }

    public List<UserDTO> getAllUsers() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            List<User> userList = query.getResultList();

            List<UserDTO> userDTOList = new ArrayList<>();
            for (User user : userList) {
                userDTOList.add(new UserDTO(user));
            }
            return userDTOList;
        } finally {
            em.close();
        }
    }


    public UserDTO getUserById(Long id) {
        EntityManager em = getEntityManager();
        User user = em.find(User.class, id);
        return new UserDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO){
        EntityManager em = getEntityManager();
        User user = new User(userDTO);
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        System.out.println(user);
        return new UserDTO(user);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = new User(userDTO);
        user.getRoleList().forEach(role -> {
            role.getUserList().add(user);
        });

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }

    public UserDTO deleteUser(Long id) {
        EntityManager em = emf.createEntityManager();
       User user = em.find(User.class, id);
        try {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }
}
