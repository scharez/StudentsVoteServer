package repository;

import entity.CandidateVote;
import jwt.JwtBuilder;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Repository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    //
    private List<CandidateVote> cvs = new ArrayList<>();
    private List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c").getResultList();
    private int candidateCounter = 0;
    //

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

        String sometoken;
        if(lu.isTeacher()){
            sometoken = new JwtBuilder().create(user.getUsername());
            if(isReturningOfficer(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.ADMIN, sometoken);
            } else {
                return jsonLoginBuilder(user.getUsername(), Role.Teacher, sometoken);
            }
        } else {
            sometoken = new JwtBuilder().create(user.getUsername());
            if(isCandidate(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.Candidates, sometoken);
            } else {
                return jsonLoginBuilder(user.getUsername(), Role.Students, sometoken);
            }
        }


    }

    private String jsonLoginBuilder(String username, Role role, String token) {

        JSONObject user = new JSONObject();

        user
            .put("username", username)
            .put("role", role)
            .put("token", token);

        System.out.println(user.toString());

        return user.toString();
    }



    private boolean isCandidate(String username) {
        List<Candidate> username2 = em.createQuery("SELECT c FROM Candidate c WHERE c.username = :username", Candidate.class).setParameter("username", username).getResultList();
        return !username2.isEmpty();
    }

    private boolean isReturningOfficer(String username) {
        List<ReturningOfficer> username2 = em.createQuery("SELECT rs FROM ReturningOfficer rs WHERE rs.username = :username", ReturningOfficer.class).setParameter("username", username).getResultList();

        return !username2.isEmpty();
    }


    public String setCandidate(Candidate candidate) {
        em.getTransaction().begin();
        em.persist(candidate);
        em.getTransaction().commit();
        return "got it";
    }

    public String getCandidate(boolean full) {


        List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();
        if(full){
            return jsonCandidate(candidates);
        } else {
            return jsonfullCandidate(candidates);
        }
    }

    private String jsonfullCandidate(List<Candidate> candidates) {

        JSONObject jsonC = new JSONObject();
        int i = 0;

        for(Candidate candidate : candidates){
            System.out.println(candidate.getFirstname());
            jsonC.put("can" + i + "_username", candidate.getUsername())
                    .put("can" + i + "_firstname", candidate.getFirstname())
                    .put("can" + i + "_lastname", candidate.getLastname())
                    .put("can" + i + "_zweig", candidate.getAbteilung())
                    .put("can" + i + "_klasse", candidate.getCandidateClass())
                    .put("can" + i + "_wahlversprechen", candidate.getElectionPromise())
                    .put("can" + i + "_bild", candidate.getPicture());
            i++;
        }
        i = 0;
        return jsonC.toString();
    }

    private String jsonCandidate(List<Candidate> candidates) {

        JSONObject jsonC = new JSONObject();
        int i = 0;

        for(Candidate candidate : candidates){
            System.out.println(candidate.getFirstname());
            jsonC.put("can" + i + "_username", candidate.getUsername())
                 .put("can" + i + "_firstname", candidate.getFirstname())
                 .put("can" + i + "_lastname", candidate.getLastname());
            i++;
        }
        i = 0;
        return jsonC.toString();
    }


    public void parseJson(String json) {
        JSONObject singleVote = new JSONObject(json);

        String username = singleVote.getString("id");
        int score = singleVote.getInt("score");
        String schoolClass = singleVote.getString("class");

        if(cvs.size() < candidates.size()) {
            boolean found = false;
            for(CandidateVote cv : cvs) {
                if(cv.getCandidate().getUsername().equals(username) && cv.getSchoolClass().equals(schoolClass)) {
                    found = true;
                }
            }
            if(!found) {
                for(Candidate c : this.candidates) {
                    if(c.getUsername().equals(username)) {
                        cvs.add(new CandidateVote(c, schoolClass));
                    }
                }
            }
        }

        for(CandidateVote cv : this.cvs) {
            if(cv.getCandidate().getUsername().equals(username) && cv.getSchoolClass().equals(schoolClass)) {
                cv.addScore(score);
            }
        }

    }

    /*public void saveimage(File file, int id) {
        em.getTransaction().begin();
        Candidate can = em.find(Candidate.class, id);
        can.setPicture(file);
        em.merge(can);
        em.getTransaction().commit();
    }*/

    public void endelection() {
        List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();
        List<CandidateVote> candidateVotes = em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList();
        int score = 0;
        int first = 0;
        em.getTransaction().begin();
        for(Candidate candidate : candidates){

             for(CandidateVote candidateVote : candidateVotes){
                 if(candidateVote.getCandidate() == candidate){
                     score = score + candidateVote.getScore();
                     first = first + candidateVote.getFirst();
                 }
             }
             candidate.setVotes(score);
             candidate.setFirst(first);
             em.merge(candidate);
        }
        em.getTransaction().commit();

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
