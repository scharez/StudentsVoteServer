package repository;

import data.entity.*;
import data.enums.ElectionState;
import data.enums.ElectionType;
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

    public String loginCheck(String username, String password) {
        CustomException ce = new CustomException();
        LdapUser lu;
        try {
            lu = new LdapUser(username, password.toCharArray());
        } catch (LdapException e) {
            return ce.buildException(503, "Service Unavailable", "LDAP not working");
        } catch (LdapAuthException e) {
            return ce.buildException(401, "Unauthorized", "Login Error");
        }
        String token;
        if (lu.isTeacher()) {
            token = new JwtBuilder().create(username);
            if (isReturningOfficer(username)) {
                return jsonLoginBuilder(username, Role.ADMIN, token);
            } else {
                try {
                    Election e = (Election) em.createQuery("SELECT MAX(e.currentDate) FROM Election e").getSingleResult();
                    if (e.getElectionState().equals(ElectionState.RUNNING)) {
                            return jsonLoginBuilder(username, Role.Teacher, token);
                    } else {
                        return jsonLoginBuilder(username, Role.Students, token);
                    }
                } catch(Exception e){
                    return jsonLoginBuilder(username, Role.Students, token);
                }
            }
        } else {
            token = new JwtBuilder().create(username);
            if (isCandidate(username)) {
                return jsonLoginBuilder(username, Role.Candidates, token);
            } else {
                return jsonLoginBuilder(username, Role.Students, token);
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
          return "Failed to upload CSV";
        }
        em.getTransaction().begin();
        for (String name : lines) {
          em.persist(new SchoolClass(name, new Date().toString()));
        }
        em.getTransaction().commit();
        System.out.println(file.getName());
        return "CSV uploaded";
    }

    public String getCurrentVoteDate(ElectionType electionType) {
        String currentVoteDate = em.createQuery("SELECT MAX(e.currentDate) FROM Election e WHERE e.electionType = :electionType")
                .setParameter("electionType", electionType)
                .getSingleResult()
                .toString();
        if(currentVoteDate == null) {
            return "No election found";
        }
        System.out.println(currentVoteDate);
        return currentVoteDate;
    }
}
