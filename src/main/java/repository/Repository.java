package repository;

import ldapuser.LdapAuthException;
import ldapuser.LdapException;
import ldapuser.LdapUser;
import org.json.JSONObject;
import utils.CustomException;
import utils.Role;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

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



    public String loginCheck(String username, String password) {

        Role role;
        CustomException ce = new CustomException();

        try {
            LdapUser lu = new LdapUser(username, password.toCharArray());
        } catch (LdapException e) {
            e.printStackTrace();

             return ce.buildException(500, "LDAP not working");

        } catch (LdapAuthException e) {
            e.printStackTrace();
            return ce.buildException(400, "Login Error");
        }


        return jsonLoginBuilder(username, Role.Teacher);
    }

    private String jsonLoginBuilder(String username, Role role) {

        JSONObject user = new JSONObject();

        user.put("user",new JSONObject())
            .put("username", username)
            .put("role", role)
            .put("token","muss noch generiert werden lul");

        return user.toString();
    }

    /*
    private boolean isCandidate() {
        return em.find(Candidate.class, EVSBridge.getInstance().getStudentId()).getUsername() == null;
    }
    */


    /*
    public String changereturningofficer(ReturningOfficer rs) {
        rOfficer = new ReturningOfficer();
        rOfficer = rs;

        JsonObject rofficer = Json.createObjectBuilder()
                .add("username", rOfficer.getUsername())
                .build();

        return rs.toString();
    }

    */

    public String setCandidate() {

        return null;
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
