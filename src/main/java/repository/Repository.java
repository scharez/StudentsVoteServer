package repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.*;
import jwt.JwtBuilder;
import ldapuser.LdapAuthException;
import ldapuser.LdapException;
import ldapuser.LdapUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import utils.CustomException;
import utils.Point;
import utils.Role;
import utils.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;


public class Repository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private List<CandidateVote> cvs = new ArrayList<>();

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
                    Election e = (Election) em.createQuery("SELECT MAX(e.date) FROM Election e").getSingleResult();
                    if (e.getElectionState() == 2) {
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

    // Nachdem der Wahlleiter einen Kandidaten eingetragen hat

    /**
     * Persists a new Candidate and creates the corresponding Result
     *
     * @param candidate Candidate
     * @return a String
     */
    public String setCandidate(Candidate candidate) {
        List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();
        for (Candidate c : candidates) {
            if (candidate.getUsername().equals(c.getUsername())) {
                System.out.println("failed to insert");
                return "{\"response\":\"Failed: Candidate already inserted.\"";
            }
        }
        em.getTransaction().begin();
        em.persist(candidate);
        em.getTransaction().commit();
        System.out.println("candidate inserted");
        return "{\"response\":\"Candidate set.\"";
    }

    /**
     * Returns all Candidates
     *
     * @return a stringyfied JSON of all Candidates
     */
    public String getCandidates() {
        return new JSONArray(em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList()).toString();
    }

    // Nachdem der Lehrer die Klasse angegeben hat || Nachdem der Wahlleiter die Klasse angegeben hat, deren Cvs gelöscht werden sollen

    /**
     * Creates and temporarily saves a CV in a SchoolClass for each Candidate
     *
     * @param schoolClass String of SchoolClass name
     * @return a String
     */
    public String instanceCVs(String schoolClass) {
        this.cvs.clear();
        for (Candidate candidate : em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList()) {
            cvs.add(new CandidateVote(candidate, em.createQuery("SELECT sc FROM SchoolClass sc WHERE sc.name = :schoolClass", SchoolClass.class).setParameter("schoolClass", schoolClass).getSingleResult()));
        }
        return "{\"response\":\"CVs created.\"";
    }

    // Nachdem der Lehrer einen einzelnen Zettel bestätigt

    /**
     * Temporarily saves the Points from a single voting paper
     *
     * @param points Point[]
     * @return a String
     */
    public String parseJson(Point[] points) {
        Election e = (Election) em.createQuery("SELECT MAX(e.date) FROM Election e").getSingleResult();
        if (e.getElectionState() != 0) {
            for (int i = 0; i < points.length; i++) {
                for (CandidateVote cv : cvs) {
                    if (cv.getCandidate().getUsername().equals(points[i].getId())) {
                        cv.setScore(cv.getScore() + points[i].getScore());
                        if ((cv.getCandidate().getPosition().equals("s") && points[i].getScore() == 6) || (cv.getCandidate().getPosition().equals("a") && points[i].getScore() == 2)) {
                            cv.setFirst(cv.getFirst() + 1);
                        }
                    }
                }
            }
            return "{\"response\":\"Points added.\"}";
        }
        return "{\"response\":\"Election already ended.\"}";
    }

    // Nachdem der Lehrer die Wahl in einer Klasse beendet || Nachdem der Wahlleiter den Nachtrag einer Klasse beendet

    /**
     * Persist the CVs of a SchoolClass
     *
     * @return a String
     */
    public String persistCVs() {
        em.getTransaction().begin();
        for (CandidateVote cv : cvs) {
            em.persist(cv);
        }
        em.getTransaction().commit();
        return "{\"response\":\"CVs comitted.\"";
    }

    // Nachdem der Wahlleiter die Diagramme lädt

    /**
     * Return current Results and SchoolClasses that already voted
     *
     * @return a String (Json) of the Results and SchoolClasses
     */
    public String getCVs() {
        List<JSONObject> toReturn = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            toReturn.add(i, new JSONObject());
        }
        Election e = (Election) em.createQuery("SELECT MAX(e.date) FROM Election e").getSingleResult();
        for (Candidate candidate : e.getCandidates()) {
            for (CandidateVote candidateVote : em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList()) {
                if (candidate.equals(candidateVote.getCandidate())) {
                    candidate.setScore(candidate.getScore() + candidateVote.getScore());
                    candidate.setFirst(candidate.getFirst() + candidateVote.getFirst());
                }
                if (!toReturn.get(4).has(candidateVote.getSchoolClass().getName())) {
                    toReturn.get(4).put(candidateVote.getSchoolClass().getName(), 0);
                }
            }
            if (candidate.getPosition().equals("s")) {
                toReturn.get(0).put(candidate.getLastname(), candidate.getScore());
                toReturn.get(1).put(candidate.getLastname(), candidate.getFirst());
            } else {
                toReturn.get(2).put(candidate.getLastname(), candidate.getScore());
                toReturn.get(3).put(candidate.getLastname(), candidate.getFirst());
            }
        }
        return toReturn.toString();
    }

    // Nachdem der Wahlleiter die Klasse angegeben hat, deren Cvs gelöscht werden sollen

    /**
     * Delete previously persisted CVs
     *
     * @param schoolClass String of the SchoolClass's name who's Results must be removed
     * @return a String
     */
    public String deleteCVs(String schoolClass) {
        for (CandidateVote cv : em.createQuery("SELECT cv FROM CandidateVote cv WHERE cv.schoolClass.name = :schoolClass", CandidateVote.class).setParameter("schoolClass", schoolClass).getResultList()) {
            em.getTransaction().begin();
            em.remove(cv);
            em.getTransaction().commit();
        }
        return "{\"response\":\"All entries from class " + schoolClass + " deleted.\"";
    }

    // Nachdem der Wahlleiter die Wahl für alle beendet

    /**
     * End the election, calculate and persist the result
     *
     * @return a String
     */
    public String endElection() {
        em.getTransaction().begin();
        Election e = (Election) em.createQuery("SELECT MAX(e.date) FROM Election e").getSingleResult();
        for (Candidate candidate : e.getCandidates()) {
            for (CandidateVote candidateVote : em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList()) {
                if (candidateVote.getCandidate().equals(candidate)) {
                    candidate.setScore(candidate.getScore() + candidateVote.getScore());
                    candidate.setFirst(candidate.getFirst() + candidateVote.getFirst());
                }
            }
            em.merge(candidate);
        }
        e.setElectionState(0);
        em.merge(e);
        em.getTransaction().commit();
        return "{\"response\":\"Results commited.\"";
    }

    // Nachdem der Wahlleiter die Wahl startet

    /**
     * Starts the election, makes Teacher able to log in
     *
     * @return a String
     */
    public String startElection() {
        em.getTransaction().begin();
        Election e = em.createQuery("SELECT e FROM Election e", Election.class).getSingleResult();
        e.setElectionState(2);
        em.merge(e);
        em.getTransaction().commit();
        return "{\"response\":\"Election started.\"";
    }

    // Nachdem der Wahlleiter die Wahl für die Lehrer beendet

    /**
     * End the election for the Teacher, makes them unable to log in
     *
     * @return a String
     */
    public String endElectionTeacher() {
        em.getTransaction().begin();
        Election e = em.createQuery("SELECT e FROM Election e", Election.class).getSingleResult();
        e.setElectionState(1);
        em.merge(e);
        em.getTransaction().commit();
        return "{\"response\":\"Election for Teacher ended.\"";
    }



}
