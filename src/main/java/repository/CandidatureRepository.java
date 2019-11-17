package repository;

import data.dto.CandidatureDTO;
import data.entity.*;
import javax.persistence.*;

public class CandidatureRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private static CandidatureRepository instance;

    public static CandidatureRepository getInstance() {
        if (instance == null) {
            instance = new CandidatureRepository();
        }
        return instance;
    }

    public String createCandidature(CandidatureDTO candidatureDTO) {
        try {
            Candidate candidate =
                    em.createQuery("SELECT c FROM Candidate c WHERE c.username = :username", Candidate.class)
                            .setParameter("username", candidatureDTO.getUsername())
                            .getSingleResult();
            Election election =
                    em.createQuery("SELECT e FROM Election e WHERE e.currentDate = :date AND e.electionType = :electionType", Election.class)
                            .setParameter("date", candidatureDTO.getDate())
                            .setParameter("electionType", candidatureDTO.getElectionType())
                            .getSingleResult();
            SchoolClass schoolClass =
                    em.createQuery("SELECT sc FROM SchoolClass sc WHERE sc.name = :schoolClassName AND sc.ded = :date", SchoolClass.class)
                            .setParameter("schoolClassName", candidatureDTO.getSchoolClassName())
                            .setParameter("date", candidatureDTO.getDate())
                            .getSingleResult();

            em.getTransaction().begin();
            em.persist(new Candidature(
                    candidate,
                    election,
                    schoolClass,
                    candidatureDTO.getPicture(),
                    candidatureDTO.getElectionPromise()
            ));
            em.getTransaction().commit();
            return "Candidatue successfully created.";
        } catch(Exception e) {
            e.printStackTrace();
            return "Failed to create Candidature";
        }
    }

}
