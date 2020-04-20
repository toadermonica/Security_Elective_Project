package Models;

import java.security.PrivateKey;
import java.security.PublicKey;

public class User {

    private String username;
    private String password;
    private String salt;
    private Object privateKey;
    private Object publicKey;
    private boolean isLoggedIn;

    /**
     * Getters and Setters
     * @return
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
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
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public Object getPrivateKey() {
        return privateKey;
    }
    public void setPrivateKey(Object privateKey) {
        this.privateKey = privateKey;
    }
    public Object getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(Object publicKey) {
        this.publicKey = publicKey;
    }
}
