package repository;

import entity.Candidate;
import entity.ReturningOfficer;
import ldapuser.LdapAuthException;
import ldapuser.LdapException;
import ldapuser.LdapUser;
import org.json.JSONObject;
import utils.CustomException;
import utils.Role;
import utils.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Repository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();



    private static Repository instance;

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }



    public String loginCheck(User user) {

        CustomException ce = new CustomException();

        LdapUser lu;

        try {
            lu = new LdapUser(user.getUsername(), user.getPassword().toCharArray());

        } catch (LdapException e) {
             return ce.buildException(503, "Service Unavailable", "LDAP not working");
        } catch (LdapAuthException e) {
            return ce.buildException(401, "Unauthorized", "Login Error");
        }

        if(lu.isTeacher()){
            if(isReturningOfficer(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.ADMIN);
            } else {
                return jsonLoginBuilder(user.getUsername(), Role.Teacher);
            }
        } else {
            if(isCandidate(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.Candidates);
            } else {
                return jsonLoginBuilder(user.getUsername(), Role.Students);
            }
        }


    }

    private String jsonLoginBuilder(String username, Role role) {

        JSONObject user = new JSONObject();

        user
            .put("username", username)
            .put("role", role)
            .put("token","muss noch generiert werden lul");

        System.out.println(user.toString());

        return user.toString();
    }



    public boolean isCandidate(String username) {
        return false;//em.find(Candidate.class, username).getUsername() != null;
    }

    public boolean isReturningOfficer(String username) {
        return false;//em.find(ReturningOfficer.class, username).getUsername() != null;
    }


    public String setCandidate(Candidate candidate) {
        em.getTransaction().begin();
        em.persist(candidate);
        em.getTransaction().commit();
        return "got it";
    }


   /* public String changereturningofficer(String username_old, String password_old, String username_new, String password_new) {
        ReturningOfficer rsold = new ReturningOfficer(1, password_old, username_old);
        ReturningOfficer rsnew = new ReturningOfficer(1, password_new, username_new);

        em.getTransaction().begin();
        if(em.find(ReturningOfficer.class, 1) == rsold){

            em.remove(rsold);
            em.persist(rsnew);
            em.getTransaction().commit();
            return "changed Returning Officer";
        }
        em.getTransaction().commit();
        return "wrong Returning Officer";
    } */
}
