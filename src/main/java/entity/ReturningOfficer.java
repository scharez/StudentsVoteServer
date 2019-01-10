package entity;

public class ReturningOfficer {

    String username;
    String password;
    String email;

    public ReturningOfficer(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public ReturningOfficer() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
