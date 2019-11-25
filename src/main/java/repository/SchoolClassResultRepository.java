package repository;

import data.dto.SchoolClassResultDTO;
import data.entity.Candidature;
import data.entity.SchoolClass;
import data.entity.SchoolClassResult;
import data.enums.ElectionType;
import org.json.JSONArray;
import org.json.JSONObject;

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
                    em.createQuery("SELECT sc FROM SchoolClass sc WHERE sc.name = :schoolClassName AND sc.ded = :date", SchoolClass.class)
                            .setParameter("schoolClassName", schoolClassResultDTO.getSchoolClassName())
                            .setParameter("date", schoolClassResultDTO.getDate())
                            .getSingleResult();
            SchoolClassResult schoolClassResult = new SchoolClassResult(
                    schoolClass,
                    schoolClassResultDTO.getScore()
            );
            em.getTransaction().begin();
            em.persist(schoolClassResult);
            Candidature candidature = em.createQuery("SELECT cu FROM Candidature cu WHERE cu.candidate.username = :username", Candidature.class)
                    .setParameter("username", schoolClassResultDTO.getUsername())
                    .getSingleResult();
            candidature
                    .getSchoolClassResults()
                    .add(schoolClassResult);
            em.persist(candidature);
            em.getTransaction().commit();
            return "Successfully created SchoolClassResult.";
        } catch(Exception e) {
            return "Failed to create SchoolClassResult";
        }
    }

    // Returns a List SchoolClasses that still need to vote.
    public String getVotingClasses() {
        List<SchoolClass> schoolClasses = em.createQuery("SELECT sc FROM SchoolClass sc", SchoolClass.class).getResultList();
        for(SchoolClassResult schoolClassResult : em.createQuery("SELECT scr FROM SchoolClassResult scr", SchoolClassResult.class).getResultList()) {
            if(schoolClasses.contains(schoolClassResult.getSchoolClass())) {
                schoolClasses.remove(schoolClassResult.getSchoolClass());
            }
        }
        JSONArray json = new JSONArray(schoolClasses);
        System.out.println(json.toString());
        return json.toString();
    }

    // Returns a List SchoolClasses that already voted.
    public String getFinishedClasses() {
        List<SchoolClass> schoolClasses = new ArrayList<>();
        for(SchoolClassResult schoolClassResult : em.createQuery("SELECT scr FROM SchoolClassResult scr", SchoolClassResult.class).getResultList()) {
            if(!schoolClasses.contains(schoolClassResult.getSchoolClass())) {
                schoolClasses.add(schoolClassResult.getSchoolClass());
            }
        }
        for (SchoolClass sc : schoolClasses) {
          System.out.println(sc.getName());
        }
      JSONArray json = new JSONArray(schoolClasses);
      System.out.println(json.toString());
        return json.toString();
    }

    // Deletes all SchoolClassResults from a SchoolClass
    public String deleteSchoolClassResult(String schoolClassName) {
        for(SchoolClassResult schoolClassResult : em.createQuery("SELECT scr FROM SchoolClassResult scr", SchoolClassResult.class).getResultList()) {
            if(schoolClassResult.getSchoolClass().getName().equals(schoolClassName)) {
                em.getTransaction().begin();
                em.remove(schoolClassResult);
                em.getTransaction().commit();
                return "Results of SchoolClass " + schoolClassName + " deleted.";
            }
        }
        return "No results for SchoolClass " + schoolClassName + " found.";
    }

    // Returns the current SchoolClassResults
    public String getSchoolClassResults(String date, ElectionType electionType) {
        List<JSONObject> toReturn = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            toReturn.add(i, new JSONObject());
        }
        List<Candidature> candidatures =
                em.createQuery("SELECT cu FROM Candidature cu WHERE cu.election.currentDate = :date AND cu.election.electionType = :electionType", Candidature.class)
                        .setParameter("date", date)
                        .setParameter("electionType", electionType)
                        .getResultList();
        for (Candidature candidature : candidatures) {
            int score = 0;
            int first = 0;
            for (SchoolClassResult schoolClassResult : candidature.getSchoolClassResults()) {
                score += schoolClassResult.getScore();
                if(candidature.getElection().getElectionType().equals(ElectionType.SCHULSPRECHER) && score == 6) {
                    first++;
                } else if(candidature.getElection().getElectionType().equals(ElectionType.ABTEILUNGSSPRECHER) && score == 2) {
                    first++;
                }
                if (!toReturn.get(2).has(schoolClassResult.getSchoolClass().getName())) {
                    toReturn.get(2).put(schoolClassResult.getSchoolClass().getName(), 0);
                }
            }
            toReturn.get(0).put(candidature.getCandidate().getLastname(), score);
            toReturn.get(1).put(candidature.getCandidate().getLastname(), first);
        }
        return toReturn.toString();
    }

  public void deleteClass() {

  }
}
