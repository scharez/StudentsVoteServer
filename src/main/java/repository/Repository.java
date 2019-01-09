package repository;

public class Repository {

    public String loginCheck(String username, String password) {
        if(username.equals("")) {
            if(password.equals("")) {
                return "login successful";
            } else {
                return "invalid password";
            }
        } else {
            return "invalid username";
        }
    }

}
