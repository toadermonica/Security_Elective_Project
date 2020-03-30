package Libraries;
import org.bouncycastle.util.encoders.Hex;
import java.security.*;

public class Signature {

    public void addSignature(KeyPair keyPair){
        byte[] input = Hex.decode("a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7");
        byte[] inputModified = Hex.decode("a0a1a2a3a4a5a6a7");

        try {
            java.security.Signature signature =
                    java.security.Signature.getInstance("SHA256withRSA", "BC");
            signature.initSign(keyPair.getPrivate());
            signature.update(input);
            byte[] byteSign = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean verifySignature (KeyPair keyPair, byte[] byteSign){
        byte[] input = Hex.decode("a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7");
        byte[] inputModified = Hex.decode("a0a1a2a3a4a5a6a7");
        java.security.Signature verifier = null;
        Boolean isValidSignature = null;
        try {
            verifier = java.security.Signature.getInstance("SHA256withRSA", "BC");
            verifier.initVerify(keyPair.getPublic());
            verifier.update(input);
            isValidSignature = verifier.verify(byteSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidSignature;
    }
}
