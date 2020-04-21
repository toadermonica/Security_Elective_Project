package Libraries;
        import Models.User;
        import org.bouncycastle.jce.provider.BouncyCastleProvider;

        import java.math.BigInteger;
        import java.security.*;
        import java.security.interfaces.RSAPrivateKey;
        import java.security.interfaces.RSAPublicKey;
        import java.security.spec.RSAPrivateKeySpec;

public class UserRSAKeys {
    /**
     * Generating the public and private RSA keys
     * @return Object[0] privateKey and Object[1] publicKey
     */
    public KeyPair generate() {
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
    public User assignUserRSAKeys(){
        KeyPair generatedKeys = generate();
        RSAPrivateKey privateKey = (RSAPrivateKey) generatedKeys.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) generatedKeys.getPublic();
        byte[] arraymodulus = publicKey.getModulus().toByteArray();
        byte[] publicExponent = publicKey.getPublicExponent().toByteArray();
        byte[] privateExponent = privateKey.getPrivateExponent().toByteArray();
        User newUserObject = new User();
        newUserObject.setArraymodulus(arraymodulus);
        newUserObject.setArrayPrivateExponent(privateExponent);
        newUserObject.setArrayPublicExponent(publicExponent);
        return newUserObject;
    }
    public PrivateKey computerUserPrivateKey(User currentUser) {
        System.out.println("Current user is: "+currentUser);
        BigInteger exponent = new BigInteger(currentUser.getArrayPrivateExponent());
        BigInteger modulus = new BigInteger(currentUser.getArraymodulus());
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
        PrivateKey privKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privKey;
    }
    public void computeUserPublicKey(byte[]publicExponent, byte[]modulus){

    }
}
