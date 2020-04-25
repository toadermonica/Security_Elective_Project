package Models;

public class UserFiles {
    private String name, secret, status;
    private boolean signedStatus;
    private byte[] signature;

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
