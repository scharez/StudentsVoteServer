package repository;

import entity.Candidate;
import entity.ReturningOfficer;
import entity.User;
import utils.EVSBridge;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentsVotePU");
    private static EntityManager em = emf.createEntityManager();

    private ReturningOfficer rOfficer = new ReturningOfficer();
    private List<User> users = new ArrayList<>();
    Candidate candidate = new Candidate();

    private static Repository instance;
    private Repository() {
    }
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();

        }

        return instance;
    }

    public String loginCheck(String username, String password) {
        EVSBridge evs_svbridge = EVSBridge.getInstance();

        if(evs_svbridge.login(username, password)){
            System.out.println(evs_svbridge.getRole());
            return EVStoJson(true);
        }

        return EVStoJson(false);
    }

    private String EVStoJson(boolean withrightuser) {
        JsonObject theuser;
        EVSBridge evs_sv_bridge = EVSBridge.getInstance();
        String rolee = "";
        String usernamee = "" + evs_sv_bridge.getStudentId();

            if("Students".equals(evs_sv_bridge.getRole())){
                iscandidate();
                rolee += "Candidates";
            } else{
                rolee += evs_sv_bridge.getRole();
            }

            theuser = Json.createObjectBuilder()
                    .add("username", usernamee)
                    .add("role", rolee)
                    .build();

        return theuser.toString();
    }

    private boolean iscandidate() {
        return em.find(Candidate.class, EVSBridge.getInstance().getStudentId()).getUsername() == null;
    }



    public String changereturningofficer(ReturningOfficer rs) {
        rOfficer = new ReturningOfficer();
        rOfficer = rs;

        JsonObject rofficer = Json.createObjectBuilder()
                .add("username", rOfficer.getUsername())
                .build();

        return rs.toString();
    }

    public String setCandidate() {

        return null;
    }
}

/*

if(user.getPassword().equals("")) {
            if(user.getPassword().equals("")) {
                return "login successful";
            } else {
                return "invalid password";
            }
        } else {
            return "invalid username";
        }
 */
