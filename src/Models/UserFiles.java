package Models;

public class UserFiles {
    String name, location, file, secret, status;
    boolean signedStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getSignedStatus() {
        return signedStatus;
    }

    public void setSignedStatus(boolean signedStatus) {
        this.signedStatus = signedStatus;
    }
}
