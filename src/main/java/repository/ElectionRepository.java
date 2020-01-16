package repository;

import data.entity.Candidature;
import data.entity.Election;
import data.enums.ElectionState;
import data.enums.ElectionType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class ElectionRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private static ElectionRepository instance;

    public static ElectionRepository getInstance() {
        if (instance == null) {
            instance = new ElectionRepository();
        }
        return instance;
    }

    public String createElection(String date, ElectionType electionType) {
        /*for(Election election : em.createQuery("SELECT e FROM Candidate e", Election.class).getResultList()) {
            if(!election.getElectionType().equals(ElectionType.STICHWAHL)) {
                if(election.getDate().equals(date) && election.getElectionType().equals(electionType)) {
                    return "Failed to create Election.";
                }
            }
        }*/

        Election e = new Election();
        e.setElectionType(electionType);
        e.setcurrentDate(date);

        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        return "Election successfully created.";
    }

    // Teachers can now start voting.
    public String startElection(String date, ElectionType electionType) {
        Election e = em.createQuery("SELECT MAX(e.currentDate) FROM Election e WHERE e.currentDate = :date AND e.electionType = :electionType", Election.class)
                .setParameter("date", date)
                .setParameter("electionType", electionType)
                .getSingleResult();
        em.getTransaction().begin();
        e.setElectionState(ElectionState.RUNNING);
        em.merge(e);
        em.getTransaction().commit();
        return "Election started";
    }

    // Teachers can no longer vote.
    public String endElectionTeacher(String date, ElectionType electionType) {
        Election e = em.createQuery("SELECT MAX(e.currentDate) FROM Election e WHERE e.currentDate = :date AND e.electionType = :electionType", Election.class)
                        .setParameter("date", date)
                        .setParameter("electionType", electionType)
                        .getSingleResult();
        em.getTransaction().begin();
        e.setElectionState(ElectionState.STOPPED);
        em.merge(e);
        em.getTransaction().commit();
        return "Election for Teacher ended";
    }

    // The election is finalized and no results can be altered.
    public String endElection(String date, ElectionType electionType) {
        Election e = em.createQuery("SELECT MAX(e.currentDate) FROM Election e WHERE e.currentDate = :date AND e.electionType = :electionType", Election.class)
                .setParameter("date", date)
                .setParameter("electionType", electionType)
                .getSingleResult();
        em.getTransaction().begin();
        e.setElectionState(ElectionState.ENDED);
        em.merge(e);
        em.getTransaction().commit();
        return "Election finished";
    }

}
