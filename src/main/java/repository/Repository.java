package repository;

import entity.ReturningOfficer;
import objects.LoginCredential;

public class Repository {

    private ReturningOfficer rOfficer = new ReturningOfficer();
    private LoginCredential user = new LoginCredential();

    private static Repository instance;
    private Repository() {
    }
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public String loginCheck(LoginCredential user) {


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
