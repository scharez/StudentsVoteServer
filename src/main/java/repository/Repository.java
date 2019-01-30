package repository;

import entity.ReturningOfficer;
import entity.User;
import utils.EVSBridge;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private ReturningOfficer rOfficer = new ReturningOfficer();
    private List<User> users = new ArrayList<>();

    private static Repository instance;
    private Repository() {
    }
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();

        }

        return instance;
    }

    public String loginCheck(User user) {
        EVSBridge evs_svbridge = EVSBridge.getInstance();

        if(evs_svbridge.login(user.getUsername(), user.getPassword())){
            return EVStoJson(true);
        }

        return EVStoJson(false);
    }

    private String EVStoJson(boolean withrightuser) {
        JsonObject theuser;
        if(withrightuser) {
            EVSBridge evs_sv_bridge = EVSBridge.getInstance();
            theuser = Json.createObjectBuilder()
                    .add("username", evs_sv_bridge.getStudentId())
                    .add("role", evs_sv_bridge.getRole())
                    .build();
        } else {
            theuser = Json.createObjectBuilder()
                    .add("wrong_user", true)
                    .build();
        }

        return theuser.toString();
    }

    private void DeclareRole(User login) {

    }

    private void CheckRole() {

    }

    public String changereturningofficer(ReturningOfficer rs) {
        rOfficer = new ReturningOfficer();
        rOfficer = rs;

        JsonObject rofficer = Json.createObjectBuilder()
                .add("username", rOfficer.getUsername())
                .build();

        return rofficer.toString();
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
