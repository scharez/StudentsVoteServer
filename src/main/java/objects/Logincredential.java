package objects;

public class Logincredential {

    private String username;
    private String password;

    public Logincredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Logincredential(){
    }

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



}
