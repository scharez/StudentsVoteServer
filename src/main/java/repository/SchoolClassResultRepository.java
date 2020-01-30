package repository;

import data.dto.SchoolClassResultDTO;
import data.entity.Candidature;
import data.entity.Election;
import data.entity.SchoolClass;
import data.entity.SchoolClassResult;
import data.enums.ElectionType;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchoolClassResultRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private static SchoolClassResultRepository instance;

    public static SchoolClassResultRepository getInstance() {
        if (instance == null) {
            instance = new SchoolClassResultRepository();
        }
        return instance;
    }

    public String createSchoolClassResult(SchoolClassResultDTO schoolClassResultDTO) {
        try {
            SchoolClass schoolClass =
                    em.createQuery("SELECT sc FROM SchoolClass sc WHERE sc.name = :schoolClassName AND sc.currentDate = :date", SchoolClass.class)
                            .setParameter("schoolClassName", schoolClassResultDTO.getSchoolClassName())
                            .setParameter("date", schoolClassResultDTO.getDate())
                            .getSingleResult();
            SchoolClassResult schoolClassResult = new SchoolClassResult(
                    schoolClass,
                    schoolClassResultDTO.getScore(),
                    schoolClassResultDTO.getFirst()
            );
            em.getTransaction().begin();
            em.persist(schoolClassResult);
            Candidature candidature = em.createQuery("SELECT cu FROM Candidature cu WHERE cu.candidate.username = :username", Candidature.class)
                    .setParameter("username", schoolClassResultDTO.getUsername())
                    .getSingleResult();
            candidature
                    .getSchoolClassResults()
                    .add(schoolClassResult);
            em.merge(candidature);
            em.getTransaction().commit();
            return "Successfully created SchoolClassResult";
        } catch(Exception e) {
            return "Failed to create SchoolClassResult";
        }
    }

    // Returns a List SchoolClasses that still need to vote.
    public String getVotingClasses(String date, String electionType) {
        List<SchoolClass> schoolClasses = em.createQuery("SELECT sc FROM SchoolClass sc", SchoolClass.class).getResultList();
        List<Candidature> candidatures = em.createQuery("SELECT cu FROM Candidature cu WHERE cu.election.currentDate = :date AND cu.election.electionType = :electionType", Candidature.class)
                .setParameter("date", date)
                .setParameter("electionType", electionType)
                .getResultList();
        for(Candidature candidature : candidatures) {
            for(SchoolClassResult schoolClassResult : candidature.getSchoolClassResults()) {
                if(schoolClasses.contains(schoolClassResult.getSchoolClass())) {
                    schoolClasses.remove(schoolClassResult.getSchoolClass());
                }
            }
        }
        // return new Gson().toJson(schoolClasses);
        return new JSONArray(schoolClasses).toString();
    }

    // Returns a List SchoolClasses that already voted.
    public String getFinishedClasses(String date, String electionType) {
        List<SchoolClass> schoolClasses = new ArrayList<>();
        List<Candidature> candidatures = em.createQuery("SELECT cu FROM Candidature cu WHERE cu.election.currentDate = :date AND cu.election.electionType = :electionType", Candidature.class)
                .setParameter("date", date)
                .setParameter("electionType", electionType)
                .getResultList();
        for(Candidature candidature : candidatures) {
            for(SchoolClassResult schoolClassResult : candidature.getSchoolClassResults()) {
                if(!schoolClasses.contains(schoolClassResult.getSchoolClass())) {
                    schoolClasses.add(schoolClassResult.getSchoolClass());
                }
            }
        }
        return new JSONArray(schoolClasses).toString();
    }

    // Deletes all SchoolClassResults from a SchoolClass
    public String deleteSchoolClassResult(String schoolClassName, String date, String electionType) {
        List<Candidature> candidatures =
                em.createQuery("SELECT cu FROM Candidature cu WHERE cu.election.currentDate = :date AND cu.election.electionType = :electionType", Candidature.class)
                        .setParameter("date", date)
                        .setParameter("electionType", electionType)
                        .getResultList();
        for(Candidature candidature : candidatures) {
            System.out.println(candidature.getCandidate().getUsername());
            for(SchoolClassResult schoolClassResult : candidature.getSchoolClassResults()) {
                if(schoolClassResult.getSchoolClass().getName().equals(schoolClassName)) {
                    System.out.println(schoolClassResult.getSchoolClass().getName());
                    em.getTransaction().begin();
                    em.remove(schoolClassResult);
                    em.getTransaction().commit();
                    return "Results of SchoolClass " + schoolClassName + " deleted.";
                }
            }
        }
        return "No results for SchoolClass " + schoolClassName + " found.";
    }

    // Returns the current SchoolClassResults
    public String getSchoolClassResults(String date, String electionType) {
        JSONArray toReturn = new JSONArray();
        List<Candidature> candidatures =
                em.createQuery("SELECT cu FROM Candidature cu WHERE cu.election.currentDate = :date AND cu.election.electionType = :electionType", Candidature.class)
                        .setParameter("date", date)
                        .setParameter("electionType", electionType)
                        .getResultList();
        for(Candidature candidature : candidatures) {
            int score = 0;
            int first = 0;
            for(SchoolClassResult schoolClassResult : candidature.getSchoolClassResults()) {
                score += schoolClassResult.getScore();
                first += schoolClassResult.getFirst();
            }
            toReturn.put(
                new JSONObject()
                    .put("username", candidature.getCandidate().getUsername())
                    .put("firstname", candidature.getCandidate().getFirstname())
                    .put("lastname", candidature.getCandidate().getLastname())
                    .put("score", score)
                    .put("first", first)
            );
        }
        return toReturn.toString();
    }

}
