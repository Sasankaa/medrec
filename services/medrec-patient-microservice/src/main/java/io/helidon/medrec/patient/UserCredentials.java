package io.helidon.medrec.patient;


public class UserCredentials {
    public UserCredentials() {
        super();
    }
    
    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
