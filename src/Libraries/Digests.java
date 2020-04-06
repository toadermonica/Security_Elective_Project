package Libraries;


public class Digests {
    public byte[] generateMessageDigest(byte[] input){
        java.security.MessageDigest msgDigestAlg = null;
        try {
            msgDigestAlg = java.security.MessageDigest.getInstance("SHA-256", "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        msgDigestAlg.update(input);
        byte[] hashValue = msgDigestAlg.digest();
        return hashValue;
    }
    public boolean isSameHash(byte[]storedHashValue, byte[] input){
        byte [] computedHashValue = generateMessageDigest(input);
        boolean isEqualValue = java.security.MessageDigest.isEqual(storedHashValue, computedHashValue);
        return isEqualValue;
    }
}
