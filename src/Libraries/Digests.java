package Libraries;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Digests {
    public byte[] generateMessageDigest(byte[] input){
        Security.addProvider(new BouncyCastleProvider());
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
        Security.addProvider(new BouncyCastleProvider());
        byte [] computedHashValue = generateMessageDigest(input);
        boolean isEqualValue = java.security.MessageDigest.isEqual(storedHashValue, computedHashValue);
        return isEqualValue;
    }
}
