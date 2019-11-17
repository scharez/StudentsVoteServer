package repository;

import data.entity.Election;
import data.enums.ElectionState;
import data.enums.ElectionType;

import javax.persistence.*;
import java.util.Date;

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
        for(Election election : em.createQuery("SELECT e FROM Candidate e", Election.class).getResultList()) {
            if(!election.getElectionType().equals(ElectionType.STICHWAHL)) {
                /*if(election.getDate().equals(date) && election.getElectionType().equals(electionType)) {
                    return "An Election of this ElectionType was already held this year.";
                }*/
            }
        }
        em.getTransaction().begin();
        em.persist(new Election(date, electionType));
        em.getTransaction().commit();
        return "Election successfully created.";
    }

    // Teachers can now start voting.
    public String startElection() {
        em.getTransaction().begin();
        Election e = em.createQuery("SELECT e FROM Election e", Election.class).getSingleResult();
        e.setElectionState(ElectionState.RUNNING);
        em.merge(e);
        em.getTransaction().commit();
        return "Election started";
    }

    // Teachers can no longer vote.
    public String endElectionTeacher() {
        em.getTransaction().begin();
        Election e = em.createQuery("SELECT e FROM Election e", Election.class).getSingleResult();
        e.setElectionState(ElectionState.STOPPED);
        em.merge(e);
        em.getTransaction().commit();
        return "{\"response\":\"Election for Teacher ended.\"";
    }

    // The election is finalized and no results can be altered.
    public String endElection() {
        em.getTransaction().begin();
        Election e = (Election) em.createQuery("SELECT MAX(e.date) FROM Election e").getSingleResult();
        e.setElectionState(ElectionState.ENDED);
        em.merge(e);
        em.getTransaction().commit();
        return "{\"response\":\"Results commited.\"";
    }

}
