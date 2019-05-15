package repository;

import entity.Result;
import entity.CandidateVote;
import jwt.JwtBuilder;
import entity.Candidate;
import entity.ReturningOfficer;
import ldapuser.LdapAuthException;
import ldapuser.LdapException;
import ldapuser.LdapUser;
import org.json.JSONObject;
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
    private final List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c").getResultList();

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
        Candidate username2 = em.createQuery("SELECT c FROM Candidate c WHERE c.username = :username", Candidate.class).setParameter("username", username).getSingleResult();
        return !username2.equals(null);
    }

    private boolean isReturningOfficer(String username) {
        ReturningOfficer username2 = em.createQuery("SELECT rs FROM ReturningOfficer rs WHERE rs.username = :username", ReturningOfficer.class).setParameter("username", username).getSingleResult();
        return !username2.equals(null);
    }

    // Nachdem der Wahlleiter einen Kandidaten eingetragen hat
    public String setCandidate(Candidate candidate) {
        em.getTransaction().begin();
        em.persist(candidate);
        em.persist(new Result(candidate));
        em.getTransaction().commit();
        return "Candidate set.";
    }

    public List<Candidate> getCandidates() {
        return em.createQuery("SELECT c FROM Candidate c").getResultList();
    }

    // Nachdem der Lehrer die Klasse angegeben hat || Nachdem der Wahlleiter die Klasse angegeben hat, deren Cvs gelöscht werden sollen
    public String instanceCVs(String schoolClass) {
        this.cvs.clear();
        List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c").getResultList();
        for(Candidate c : candidates) {
            cvs.add(new CandidateVote(c, schoolClass));
        }
        return "CVs created.";
    }

    // Nachdem der Lehrer einen einzelnen Zettel bestätigt
    public String parseJson(Point[] points) {
        for(int i = 0; i < points.length; i++) {
            for(CandidateVote cv : cvs) {
                if(cv.getCandidate().getUsername().equals(points[i].getId())) {
                    cv.setScore(cv.getScore() + points[i].getScore());
                    if((cv.getCandidate().getPosition().equals("s") && points[i].getScore() == 6) || (cv.getCandidate().getPosition().equals("a") && points[i].getScore() == 2)) {
                        cv.setFirst(cv.getFirst() + 1);
                    }
                }
            }
        }
        return "Points added.";
    }

    // Nachdem der Lehrer die Wahl in einer Klasse beendet || Nachdem der Wahlleiter den Nachtrag einer Klasse beendet
    public String persistCVs() {
        em.getTransaction().begin();
        for(CandidateVote cv : cvs) {
            em.persist(cv);
        }
        em.getTransaction().commit();
        return "CVs comitted.";
    }

    // Nachdem der Wahlleiter die Diagramme lädt
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
    public String deleteCvs(String schoolClass) {
        for(CandidateVote cv : em.createQuery("SELECT cv FROM CandidateVote cv WHERE cv.schoolClass = :schoolClass", CandidateVote.class).setParameter("schoolClass", schoolClass).getResultList()) {
            em.getTransaction().begin();
            em.remove(cv);
            em.getTransaction().commit();
        }
        return "All entries from class " + schoolClass + " deleted.";
    }

    // Nachdem der Wahlleiter die Wahl für alle beendet
    public String endElection() {
        List<CandidateVote> candidateVotes = em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList();
        List<Result> results = em.createQuery("SELECT r FROM Result r", Result.class).getResultList();
        em.getTransaction().begin();
        for(Result result : results) {
            result.setScore(0);
            result.setFirst(0);
            for(CandidateVote candidateVote : candidateVotes) {
                if(result.getCandidate().equals(candidateVote.getCandidate())) {
                    result.setScore(result.getScore() + candidateVote.getScore());
                    result.setFirst(result.getFirst() + candidateVote.getFirst());
                    em.merge(result);
                }
            }
        }
        em.getTransaction().commit();
        return "Results commited.";
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
