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


public class Repository {

    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    //private EntityManager em = emf.createEntityManager();



    private static Repository instance;

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }



    public String loginCheck(User user) {

        CustomException ce = new CustomException();

        /*
        try {
            LdapUser lu = new LdapUser(username, password.toCharArray());
        } catch (LdapException e) {
            e.printStackTrace();

             return ce.buildException(500, "LDAP not working");

        } catch (LdapAuthException e) {
            e.printStackTrace();
            return ce.buildException(400, "Login Error");
        }
x1
        */

        if(user.getUsername().equals("test") && user.getPassword().equals("1234")) {
            if(isCandidate(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.Candidates);
            } else if(isReturningOfficer(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.ADMIN);
            }

            return jsonLoginBuilder(user.getUsername(), Role.Students);
        }

        return ce.buildException(400, "Login Error");

    }

    private String jsonLoginBuilder(String username, Role role) {

        JSONObject user = new JSONObject();

        user
            .put("username", username)
            .put("role", role)
            .put("token","muss noch generiert werden lul");

        return user.toString();
    }



    public boolean isCandidate(String username) {
        //return em.find(Candidate.class, username).getUsername() == null;
        return false;
    }

    public boolean isReturningOfficer(String username) {
        //return em.find(ReturningOfficer.class, username).getUsername() == null;
        return false;
    }


    public String setCandidate(Candidate candidate) {
        //em.getTransaction().begin();
        //em.persist(candidate);
        //em.getTransaction().commit();
        return "got it";
    }


    public String changereturningofficer(ReturningOfficer rsold, ReturningOfficer rsnew) {
        //em.getTransaction().begin();
        /*if(em.find(ReturningOfficer.class, rsold) == null){
            em.remove(rsold);
            em.persist(rsnew);
            return "changed Returning Officer";
        }*/

        return "wrong Returning Officer";
    }
}

/*

if(user.getPassword().equals("")) {
            if(user.getPassword().equals("")) {
                return "login successful";
            } else {
                return "invalid password";
            }
        } else {
            return "invalid username";
        }
 */
