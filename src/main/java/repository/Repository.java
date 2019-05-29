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
        if(lu.isTeacher()){
            token = new JwtBuilder().create(user.getUsername());
            if(isReturningOfficer(user.getUsername())){
                return jsonLoginBuilder(user.getUsername(), Role.ADMIN, token);
            } else {
                if(em.createQuery("SELECT es FROM ElectionState es", ElectionState.class).getSingleResult().isStarted()) {
                    if(!em.createQuery("SELECT es FROM ElectionState es", ElectionState.class).getSingleResult().isEnded()) {
                        return jsonLoginBuilder(user.getUsername(), Role.Students, token);
                    }
                }
                return jsonLoginBuilder(user.getUsername(), Role.Teacher, token);
            }
        } else {
            token = new JwtBuilder().create(user.getUsername());
            if(isCandidate(user.getUsername())){
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
        return !em.createQuery("SELECT c FROM Candidate c WHERE c.username = :username", Candidate.class).setParameter("username", username).getSingleResult().equals(null);
    }

    private boolean isReturningOfficer(String username) {
        return !em.createQuery("SELECT rs FROM ReturningOfficer rs WHERE rs.username = :username", ReturningOfficer.class).setParameter("username", username).getSingleResult().equals(null);
    }

    // Nachdem der Wahlleiter einen Kandidaten eingetragen hat
    /**
     * Persists a new Candidate and creates the corresponding Result
     *
     * @param candidate Candidate
     * @return a String
     */
    public String setCandidate(Candidate candidate) {
        em.getTransaction().begin();
        em.persist(candidate);
        em.persist(new Result(candidate));
        em.getTransaction().commit();
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
        for(Candidate candidate : em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList()) {
            cvs.add(new CandidateVote(candidate, schoolClass));
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
        if(em.createQuery("SELECT es FROM ElectionState es", ElectionState.class).getSingleResult().isEndedCompletely()) {
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
        for(CandidateVote cv : cvs) {
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
        for(int i = 0; i < 5; i++) {
            toReturn.add(i, new JSONObject());
        }
        for(Result result : em.createQuery("SELECT r FROM Result r", Result.class).getResultList()) {
            result.setScore(0);
            result.setFirst(0);
            for(CandidateVote candidateVote : em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList()) {
                if(result.getCandidate().equals(candidateVote.getCandidate())) {
                    result.setScore(result.getScore() + candidateVote.getScore());
                    result.setFirst(result.getFirst() + candidateVote.getFirst());
                }
                if(!toReturn.get(4).has(candidateVote.getSchoolClass())) {
                    toReturn.get(4).put(candidateVote.getSchoolClass(), 0);
                }
            }
            if(result.getCandidate().getPosition().equals("s")) {
                toReturn.get(0).put(result.getCandidate().getLastname(), result.getScore());
                toReturn.get(1).put(result.getCandidate().getLastname(), result.getFirst());
            } else {
                toReturn.get(2).put(result.getCandidate().getLastname(), result.getScore());
                toReturn.get(3).put(result.getCandidate().getLastname(), result.getFirst());
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
        for(CandidateVote cv : em.createQuery("SELECT cv FROM CandidateVote cv WHERE cv.schoolClass = :schoolClass", CandidateVote.class).setParameter("schoolClass", schoolClass).getResultList()) {
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
        for(Result result : em.createQuery("SELECT r FROM Result r", Result.class).getResultList()) {
            result.setScore(0);
            result.setFirst(0);
            for(CandidateVote candidateVote : em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList()) {
                if(result.getCandidate().equals(candidateVote.getCandidate())) {
                    result.setScore(result.getScore() + candidateVote.getScore());
                    result.setFirst(result.getFirst() + candidateVote.getFirst());
                    em.merge(result);
                }
            }
        }
        ElectionState es = em.createQuery("SELECT es FROM ElectionState es", ElectionState.class).getSingleResult();
        es.setEndedCompletely(true);
        em.merge(es);
        em.getTransaction().commit();
        return "{\"response\":\"Results commited.\"";
    }

    public String startElection() {
        em.getTransaction().begin();
        ElectionState es = em.createQuery("SELECT es FROM ElectionState es", ElectionState.class).getSingleResult();
        es.setStarted(true);
        em.merge(es);
        em.getTransaction().commit();
        return "{\"response\":\"Election started.\"";
    }

    public String endElectionTeacher() {
        em.getTransaction().begin();
        ElectionState es = em.createQuery("SELECT es FROM ElectionState es", ElectionState.class).getSingleResult();
        es.setEnded(true);
        em.merge(es);
        em.getTransaction().commit();
        return "{\"response\":\"Election for Teacher ended.\"";
    }

}
