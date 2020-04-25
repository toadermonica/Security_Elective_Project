package Utils;

import Libraries.RSAKeys;
import Models.User;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.List;
import java.util.Objects;

public class UserAuthentication {
    public static boolean SignIn(String username, String password) throws NoSuchProviderException, NoSuchAlgorithmException {
        List<User> usersList = JsonFileHandler.ReadObjectsFromJsonFile_UserRSAKeyFile();
        int length = usersList.size();
        for (int i = 0; i < length; i++){
            String userNameFromList = usersList.get(i).getUsername();
            String passwordFromList = usersList.get(i).getPassword();
            if (userNameFromList.equals(username)){
                String saltFromList = usersList.get(i).getSalt();

                Security.addProvider(new BouncyCastleProvider());
                StringBuilder passAndSalt = new StringBuilder();
                passAndSalt.append(saltFromList);
                passAndSalt.append(password);
                byte [] input = passAndSalt.toString().getBytes();
                MessageDigest mDigest = MessageDigest.getInstance("SHA-256", "BC");
                mDigest.update(input);
                byte[] hashValue = mDigest.digest();
                String hashHexString = Hex.toHexString(hashValue);
                System.out.println("Hashvalue: " + hashHexString);

                if (!hashHexString.equals(passwordFromList)){
                    return false;
                }

                return true;
            }
        }
        return false;
    }
    public static String CreateNewUser(String username, String password) throws NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        List<User> usersList = JsonFileHandler.ReadObjectsFromJsonFile_UserRSAKeyFile();
        Object[] signUpUserMessage = CreateNewUser(username, password, usersList);
        return (String) signUpUserMessage[1];
    }
    private static Object[] CreateNewUser(String username, String password, List<User> usersList) throws NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Security.addProvider(new BouncyCastleProvider());

        StringBuilder passAndSalt = new StringBuilder();
        SecureRandom rng = new SecureRandom();
        Integer salt = rng.nextInt();
        passAndSalt.append(salt.toString());

        byte [] input = password.getBytes();
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256", "BC");

        passAndSalt.append(password);
        mDigest.update(passAndSalt.toString().getBytes("UTF-8")); // Change this to "UTF-16" if needed
        System.out.println("pass and salt " + passAndSalt);
//        byte[] digest = mDigest.digest();

//        mDigest.update(input);
        byte[] hashValue = mDigest.digest();
        String hashHexString = Hex.toHexString(hashValue);
        System.out.println("Hashvalue: " + hashHexString);

        Object[] signUpStatus = new Object[2];
        int length = usersList.size();
        int count=0;
        for (int i = 0; i < length; i++){
            String userNameFromList = usersList.get(i).getUsername();
            if (userNameFromList.equals(username)){
                count++;
            }
        }
        if(count == 0){
            try{
                //create the user object with keys and username
                User newUserObject = new User();
                newUserObject.setUsername(username);
                newUserObject.setPassword(hashHexString);
                newUserObject.setSalt(salt.toString());
                newUserObject.setPrivateKey(RSAKeys.generate()[0]);
                newUserObject.setPublicKey(RSAKeys.generate()[1]);
                newUserObject.setLoggedIn(false);
                //record the new user in the user list and record that list in the file
                usersList.add(newUserObject);
                JsonFileHandler.WriteObjectsToJsonFile_UserRSAKeyFile(usersList);
                signUpStatus[0] = 1;
            }catch (Exception e){
                System.out.println("Exception was triggered: "+ e);
                signUpStatus[0] = 0;
                signUpStatus[1] = "this action failed! Please try again in a while.";
            }
        }else{
            signUpStatus[0] = 0;
            signUpStatus[1] = "We are sorry, but you already have an account! Try logging in. ";
        }
        return signUpStatus;
    }
}
