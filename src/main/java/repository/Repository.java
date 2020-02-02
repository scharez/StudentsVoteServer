package repository;

import data.entity.*;
import data.enums.Department;
import data.enums.ElectionState;
import data.enums.ElectionType;
import jwt.JwtBuilder;
import ldapuser.LdapAuthException;
import ldapuser.LdapException;
import ldapuser.LdapUser;
import org.json.JSONObject;
import utils.CustomException;
import utils.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static data.enums.Department.ELEKTRONIK;
import static data.enums.Department.INFORMATIK;


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

        ReturningOfficer returningOfficer = em.createQuery("SELECT ro FROM ReturningOfficer ro", ReturningOfficer.class).getSingleResult();
        if(returningOfficer.getUsername().equals(username) && returningOfficer.getPassword().equals(password)) {
            return jsonLoginBuilder(username, Role.ADMIN, new JwtBuilder().create(username));
        }

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
            //if (isReturningOfficer(username)) {
            //    return jsonLoginBuilder(username, Role.ADMIN, token);
            //} else {
                try {
                    Election e = (Election) em.createQuery("SELECT MAX(e.currentDate) FROM Election e").getSingleResult();
                    if (e.getElectionState().equals("RUNNING")) {
                            return jsonLoginBuilder(username, Role.Teacher, token);
                    } else {
                        return jsonLoginBuilder(username, Role.Students, token);
                    }
                } catch(Exception e){
                    return jsonLoginBuilder(username, Role.Students, token);
                }
            //}
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
        List<String> lines;
        try {
          lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
          return "Failed to upload CSV";
        }
        em.getTransaction().begin();
        for (String name : lines) {
          String department;
          if(name.substring(2).equals("HITM") || name.substring(2).equals("HIF")) {
              department = "INFORMATIK";
          } else {
              department = "ELEKTRONIK";
          }
          em.persist(new SchoolClass(
                  name,
                  department,
                  new SimpleDateFormat("dd/MM/yyyy").format(new Date())
          ));
        }
        em.getTransaction().commit();
        System.out.println(file.getName());
        return "CSV uploaded";
    }

    public String getCurrentVoteDate(String electionType) {
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

    public String createReturningOfficer(String username, String password) {
        em.getTransaction().begin();
        em.persist(new ReturningOfficer(username, password));
        em.getTransaction().commit();
        return "ReturningOfficer successfully created.";
    }

    public String updateReturningOfficer(String username, String password) {
        ReturningOfficer returningOfficer = em.createQuery("SELECT ro FROM ReturningOfficer ro", ReturningOfficer.class).getSingleResult();
        if(!username.equals("")) {
            returningOfficer.setUsername(username);
        }
        if(!password.equals("")) {
            returningOfficer.setPassword(password);
        }
        em.getTransaction().begin();
        em.merge(returningOfficer);
        em.getTransaction().commit();
        return "ReturningOfficer successfully updated.";
    }

    public List<Election> getElections() {

        List<Election> allElections = em.createQuery("SELECT e FROM Election e WHERE e.electionState != 'nix'")
                .getResultList();
        if(!allElections.isEmpty()){
            System.out.println(allElections.get(0).getElectionState());
        }

        return allElections;

    }
}
