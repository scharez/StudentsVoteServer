package repository;

import data.entity.*;
import data.enums.ElectionState;
import jwt.JwtBuilder;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Repository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private List<SchoolClassResult> cvs = new ArrayList<>();

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
        String token;
        if (lu.isTeacher()) {
            token = new JwtBuilder().create(user.getUsername());
            if (isReturningOfficer(user.getUsername())) {
                return jsonLoginBuilder(user.getUsername(), Role.ADMIN, token);
            } else {
                try {
                    Election e = (Election) em.createQuery("SELECT MAX(e.currentDate) FROM Election e").getSingleResult();
                    if (e.getElectionState().equals(ElectionState.RUNNING)) {
                            return jsonLoginBuilder(user.getUsername(), Role.Teacher, token);
                    } else {
                        return jsonLoginBuilder(user.getUsername(), Role.Students, token);
                    }
                } catch(Exception e){
                    return jsonLoginBuilder(user.getUsername(), Role.Students, token);
                }
            }
        } else {
            token = new JwtBuilder().create(user.getUsername());
            if (isCandidate(user.getUsername())) {
                return jsonLoginBuilder(user.getUsername(), Role.Candidates, token);
            } else {
                return jsonLoginBuilder(user.getUsername(), Role.Students, token);
            }
        }
    }

    private String jsonLoginBuilder(String username, Role role, String token) {
        JSONObject user = new JSONObject();
        user
                .put("username", username)
                .put("role", role)
                .put("token", token);
        return user.toString();
    }

    private boolean isCandidate(String username) {
        try {
            return !em.createQuery("SELECT c FROM Candidate c WHERE c.username = :username", Candidate.class).setParameter("username", username).getSingleResult().equals(null);
        }catch(Exception e){
            return false;
        }
    }

    private boolean isReturningOfficer(String username) {
        try {
            return !em.createQuery("SELECT rs FROM ReturningOfficer rs WHERE rs.username = :username", ReturningOfficer.class).setParameter("username", username).getSingleResult().equals(null);
        } catch(Exception e){
            return false;
        }
    }


    public String readCsvFile(File file) {

    List<String> lines = null;
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (IOException e) {
      return "Irg stimmt mit CSV nd";
    }

    em.getTransaction().begin();
    for (String name : lines) {
      em.persist(new SchoolClass(name, new Date().toString()));
    }
    em.getTransaction().commit();

    System.out.println(file.getName());

    return "CSV uploaded";
  }

    public String getCurrentVoteDate() {

        String e = em.createQuery("SELECT MAX(e.currentDate) FROM Election e").getSingleResult().toString();

        if(e == null) {
            return "Scheiße";
        }

        System.out.println(e);

        return e;
    }
}
