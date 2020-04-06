package Libraries;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import java.security.*;

public class RSAKeys {
    private KeyPair generate() {
        KeyPair keyPair = null;
        try{
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }
    public void store(KeyPair keyPair, String dir){ }

    public PublicKey getPublicKey(){
        PublicKey publicKey = generate().getPublic();
        return publicKey;
    }

    public PrivateKey getPrivateKey(){
        PrivateKey privateKey = generate().getPrivate();
        return privateKey;
    }

}
