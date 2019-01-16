package repository;

import objects.User;

public class Repository {

    public String loginCheck(User user) {

        System.out.println(user.getUsername());

        return "TEST";
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
