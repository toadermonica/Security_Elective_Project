package Models;

import java.security.PrivateKey;
import java.security.PublicKey;

public class User {

    private String username;
    private String password;
    private String salt;
    private byte [] arraymodulus;
    private byte [] arrayPublicExponent;
    private byte [] arrayPrivateExponent;
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
    public byte[] getArraymodulus() {
        return arraymodulus;
    }

    public void setArraymodulus(byte[] arraymodulus) {
        this.arraymodulus = arraymodulus;
    }

    public byte[] getArrayPublicExponent() {
        return arrayPublicExponent;
    }

    public void setArrayPublicExponent(byte[] arrayPublicExponent) {
        this.arrayPublicExponent = arrayPublicExponent;
    }

    public byte[] getArrayPrivateExponent() {
        return arrayPrivateExponent;
    }

    public void setArrayPrivateExponent(byte[] arrayPrivateExponent) {
        this.arrayPrivateExponent = arrayPrivateExponent;
    }

}
