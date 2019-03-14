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

    //
    private List<CandidateVote> cvs = new ArrayList<>();
    private final List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c").getResultList();
    //private int candidateCounter = 0;
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
        System.err.println(username2 + "lol pfusch!");

        return !username2.isEmpty();
    }


    public String setCandidate(Candidate candidate) {
        em.getTransaction().begin();
        em.persist(candidate);
        Result res = new Result(candidate);
        em.persist(res);
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


    public String instanceCVs(String schoolClass) {
        this.cvs.clear();
        List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c").getResultList();
        for(Candidate c : candidates) {
            cvs.add(new CandidateVote(c, schoolClass));
        }
        return "CVs created.";
    }

    public String parseJson(String json) {
        JSONObject singleVote = new JSONObject(json);

        String username = singleVote.getString("id");
        int score = singleVote.getInt("score");

        for(CandidateVote cv : cvs) {
            if(cv.getCandidate().getUsername().equals(username)) {
                cv.setScore(cv.getScore() + score);
                if(score == 6) {
                    cv.setFirst(cv.getFirst() + 1);
                }
            }
        }

        return score + " Points added to candidate " + username;
    }

    public String persistCVs() {
        em.getTransaction().begin();
        for(CandidateVote cv : cvs) {
            em.persist(cv);
        }
        em.getTransaction().commit();
        this.cvs.clear();
        return "CVs comitted.";
    }

    public String endElection() {
        List<CandidateVote> candidateVotes = em.createQuery("SELECT cv FROM CandidateVote cv", CandidateVote.class).getResultList();
        List<Result> results = em.createQuery("SELECT r FROM Result r", Result.class).getResultList();
        em.getTransaction().begin();
        for(CandidateVote candidateVote : candidateVotes) {
            for(Result result : results) {
                if(candidateVote.getCandidate().equals(result.getCandidate())) {
                    result.setScore(result.getScore() + candidateVote.getScore());
                    result.setFirst(result.getFirst() + candidateVote.getFirst());
                    em.merge(result);
                }
            }
        }
        em.getTransaction().commit();
        return "Results commited.";
    }

    public void endelection() {
        List<Result> results = em.createQuery("SELECT r FROM Result r", Result.class).getResultList();
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
