package repository;

import data.entity.SchoolClass;

import javax.persistence.*;
import java.util.Date;

public class SchoolClassRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private EntityManager em = emf.createEntityManager();

    private static SchoolClassRepository instance;

    public static SchoolClassRepository getInstance() {
        if (instance == null) {
            instance = new SchoolClassRepository();
        }
        return instance;
    }

    public String createSchoolClass(String name, String date) {
        for(SchoolClass schoolClass : em.createQuery("SELECT sc FROM SchoolClass sc", SchoolClass.class).getResultList()) {
            if(schoolClass.getName().equals(name) && schoolClass.getded().equals(date)) {
                return "SchoolClass already exists!";
            }
        }
        em.getTransaction().begin();
        em.persist(new SchoolClass(name, date));
        em.getTransaction().commit();
        return "SchoolClass successfully created.";
    }

}
