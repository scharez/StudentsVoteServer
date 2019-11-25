package repository;

import data.entity.Candidate;

import javax.persistence.*;

public class CandidateRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private static CandidateRepository instance;

    public static CandidateRepository getInstance() {
        if (instance == null) {
            instance = new CandidateRepository();
        }
        return instance;
    }

    public String createCandidate(String username, String firstname, String lastname) {
        for(Candidate candidate : em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList()) {
            if(candidate.getUsername().equals(username)) {
                return "Candidate already exists!";
            }
        }
        System.out.println("lol1");
        em.getTransaction().begin();
        em.persist(new Candidate(username, firstname, lastname));
        em.getTransaction().commit();
        System.out.println("lol2");
        return "Candidate successfully created.";
    }

    public String getCandidates() {
        return em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList().toString();
    }

}
