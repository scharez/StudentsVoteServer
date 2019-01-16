package repository;

import entity.ReturningOfficer;
import entity.User;
import objects.LoginCredential;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private ReturningOfficer rOfficer = new ReturningOfficer();
    private List<LoginCredential> logincredentials = new LinkedList<>();
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

    public String loginCheck(LoginCredential login) {

        if(logincredentials.contains(login.getUsername()) && logincredentials.contains(login.getPassword())){
            users.add(new User(login.getUsername()));
            DeclareRole(login);


            return Jsonrole(login) + "success";
        }

        return "Wrong username or password";
    }

    private String Jsonrole(LoginCredential login) {

        JsonObject role = Json.createObjectBuilder()
                .add("", 1)
                .build();

        return null;
    }

    private void DeclareRole(LoginCredential login) {

    }

    private void CheckRole() {

    }

    public String changereturningofficer(ReturningOfficer rs) {
        rOfficer = new ReturningOfficer();
        rOfficer = rs;

        return "You are now Registered";
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
