package repository;

import entity.ReturningOfficer;
import objects.Logincredential;

public class Repository {

    private ReturningOfficer rOfficer = new ReturningOfficer();
    private Logincredential user = new Logincredential();

    private static Repository instance;
    private Repository() {
    }
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public String loginCheck(Logincredential user) {


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
