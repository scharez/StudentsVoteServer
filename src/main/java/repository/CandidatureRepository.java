package repository;

import data.dto.CandidatureDTO;
import data.entity.*;
import org.json.JSONArray;

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
            System.out.println(candidatureDTO.getUsername());
            Candidate candidate =
                    em.createQuery("SELECT c FROM Candidate c WHERE c.username = :username", Candidate.class)
                            .setParameter("username", candidatureDTO.getUsername())
                            .getSingleResult();
            System.out.println(candidate.getUsername());
            Election election =
                    em.createQuery("SELECT e FROM Election e WHERE e.currentDate = :date AND e.electionType = :electionType", Election.class)
                            .setParameter("date", candidatureDTO.getDate())
                            .setParameter("electionType", candidatureDTO.getElectionType())
                            .getSingleResult();
            SchoolClass schoolClass =
                    em.createQuery("SELECT sc FROM SchoolClass sc WHERE sc.name = :schoolClassName AND sc.currentDate = :date", SchoolClass.class)
                            .setParameter("schoolClassName", candidatureDTO.getSchoolClassName())
                            .setParameter("date", candidatureDTO.getDate())
                            .getSingleResult();

            em.getTransaction().begin();
            if(candidate == null && !candidatureDTO.getFirstname().equals("")) {
                candidate = new Candidate(
                        candidatureDTO.getUsername(),
                        candidatureDTO.getFirstname(),
                        candidatureDTO.getLastname()
                );
                em.persist(candidate);
            }
            em.persist(new Candidature(
                    candidate,
                    election,
                    schoolClass,
                    candidatureDTO.getPicture(),
                    candidatureDTO.getElectionPromise()
            ));
            em.getTransaction().commit();
            return "Candidature successfully created.";
        } catch(Exception e) {
            e.printStackTrace();
            return "Failed to create Candidature.";
        }
    }

    public String deleteCandidature(String username) {
        try {
            Candidature candidature = em.createQuery("SELECT cu FROM Candidature cu WHERE cu.candidate.username = :username ORDER BY cu.election.currentDate DESC", Candidature.class)
                    .setParameter("username", username)
                    .getSingleResult();
            em.getTransaction().begin();
            em.remove(candidature);
            em.getTransaction().commit();
            return "Candidature successfully deleted.";
        } catch(Exception e) {
            e.printStackTrace();
            return "Failed to delete Candidature.";
        }
    }

    public String updateCandidature(CandidatureDTO candidatureDTO) {
        try {
            Candidature candidature = em.createQuery("SELECT cu FROM Candidature cu WHERE cu.candidate.username = :username ORDER BY cu.election.currentDate DESC", Candidature.class)
                    .setParameter("username", candidatureDTO.getUsername())
                    .getSingleResult();

            candidature.setSchoolClass(
                    em.createQuery("SELECT sc FROM SchoolClass sc WHERE sc.name = :schoolClassName ORDER BY sc.currentDate DESC", SchoolClass.class)
                        .setParameter("schoolClassName", candidatureDTO.getSchoolClassName())
                        .getSingleResult()
            );
            candidature.setPicture(candidatureDTO.getPicture());
            candidature.setElectionPromise(candidatureDTO.getElectionPromise());

            Candidate candidate = candidature.getCandidate();
            candidate.setUsername(candidatureDTO.getUsername());
            candidate.setFirstname(candidatureDTO.getFirstname());
            candidate.setLastname(candidatureDTO.getLastname());

            em.getTransaction().begin();
            em.merge(candidature);
            em.merge(candidate);
            em.getTransaction().commit();
            return "Candidature successfully updated";
        } catch(Exception e) {
            e.printStackTrace();
            return "Failed to update Candidature";
        }
    }

    public String getCandidatures() {
        for(Candidature candidature : em.createQuery("SELECT cu FROM Candidature cu", Candidature.class).getResultList()) {
            System.out.println(candidature.getElection().getElectionState().toString());
        }
        return new JSONArray(em.createQuery("SELECT cu FROM Candidature cu", Candidature.class).getResultList()).toString();
    }

}
