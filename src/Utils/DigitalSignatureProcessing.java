package Utils;

import Libraries.Digests;
import Libraries.UserRSAKeys;
import Libraries.DigitalSignatures;
import Models.User;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.List;


public class DigitalSignatureProcessing {

    public boolean processDigitalSignature(String fileName, String plainText){
        //TODO: 3. generate message digest - DONE BELOW
        byte[]msgDigest = generateMessageDigest(plainText);
        System.out.println("Message digest is now: "+msgDigest);
        //TODO: 4. generate message digest - DONE BELOW
        byte[]signature = encryptMessageDigestWithUserPrivateKey(msgDigest);
        System.out.println("Signature after encrypting the msg digest with private key is now: "+signature);
        //todo: 7. Add signiture to the fileList & Update the signature status of the file - DONE BELOW - this can be merged in step 6
        return updateSignatureAndStatusOfFile(fileName, signature);
    }

    public boolean verifyDigitalSignature(String fileName, String UserNameOrFileSender){
        System.out.println("The  fileName and the UserNameOfFileSender are: "+ fileName + " "+UserNameOrFileSender);
        // we loop the list of users in order to get the data to compute their public key
        JsonFileHandler jfh = new JsonFileHandler();
        // we loop and get the computed public key && validate the signature
        DigitalSignatures ds = new DigitalSignatures();
        boolean isValidSignature = ds.verifySignature(UserNameOrFileSender, fileName);
        return isValidSignature;
        // we decrypt the digital signature with the public key
        // we decrypt the file and make the hash of the received document's plain text
        // we compare the hash got from the decryption with the hash got from the plain text
    }

    /**
     * Step 5: Encrypt hash value with user's private key
     * @param msgDigest
     * @return byte[] signature
     */
    private byte[] encryptMessageDigestWithUserPrivateKey(byte[]msgDigest){
        System.out.println("This is what i get in msg digest ");
        System.out.println(getUserPrivateKey());
        PrivateKey userPrivateKey = getUserPrivateKey();
        DigitalSignatures ds = new DigitalSignatures();
        byte[] signature = ds.addSignature(msgDigest, userPrivateKey);
        if(signature==null){
            System.out.println("Something went fishy in encryptMessageDigestWithUserPrivateKey - DigitalSignatureProcessing.java");
            return null;
        }
        return signature;
    }
    /**
     * Step 3: Generate hash based on plain text of file
     * @param plainText
     * @return byte[] hash from file plain text
     */
    private byte[] generateMessageDigest(String plainText){
        if(plainText==null){
            return null;
        }
        Digests digestInstance = new Digests();
        byte[] plainTextToBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] hashValue = digestInstance.generateMessageDigest(plainTextToBytes);
        return hashValue;
    }
    /**
     * Step 7: Method to update the listoffiles signed status from false to true
     * @param unsignedFileName
     * @return
     */
   private boolean updateSignatureAndStatusOfFile(String unsignedFileName, byte[]signature){
       JsonFileHandler jsonFileHandler = new JsonFileHandler();
       boolean operationStatus = jsonFileHandler.UpdateSignatureToExistingEncryptedFile(unsignedFileName, signature);
       return operationStatus;
       //return true;
   }
    /**
     * Get existing user private key from UserRSAKeyFile where user isLogIn property (see User model) is true
     * The keys are generated and saved in this file on successful user signup process
     * @return PrivateKey
     */
    private PrivateKey getUserPrivateKey(){
        //read users from file and find the logged in user true
        JsonFileHandler jfh = new JsonFileHandler();
        UserRSAKeys rsaKeys = new UserRSAKeys();
        PrivateKey userPrivateKey = null;
        List<User> listOfUsers = jfh.ReadObjectsFromJsonFile_UserRSAKeyFile();
        for(int i=0;i<listOfUsers.size();i++){
            User currentUser = listOfUsers.get(i);
            if(currentUser.isLoggedIn()){
                userPrivateKey = rsaKeys.computerUserPrivateKey(currentUser);
            }
        }
        return userPrivateKey;
    }
}
