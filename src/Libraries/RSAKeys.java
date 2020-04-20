package Libraries;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import java.security.*;
import java.util.Objects;
import java.util.concurrent.Flow;

public class RSAKeys {
    /**
     * Generating the public and private RSA keys
     * @return Object[0] privateKey and Object[1] publicKey
     */
    public static Object[] generate() {
        KeyPair keyPair = null;
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        Object[] keysObj = new Object[2];
        try{
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            keysObj[0] = privateKey;
            keysObj[1] = publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Keys are now "+ keysObj[0]+" "+keysObj[1]);
        return keysObj;
    }

}
