package repository;

import entity.ReturningOfficer;
import entity.User;

import java.util.LinkedList;
import java.util.List;

public class Repository {

    private List<ReturningOfficer> rOfficer = new LinkedList<>();

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


        System.out.println(user.getUsername());

        return "TEST";
    }

    public String register() {



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
