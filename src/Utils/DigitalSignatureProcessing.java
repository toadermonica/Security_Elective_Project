package Utils;
import Libraries.Digests;
import Libraries.Signature;
import Models.User;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.List;


public class DigitalSignatureProcessing {

    public static boolean processDigitalSignature(String fileName){
        //TODO: 2. Decrypt the encrypted file & read and return the plain text to be used in step 3 - MISSING
        String plainText = "???";
        //TODO: 3. generate message digest - DONE BELOW
        byte[]msgDigest = generateMessageDigest(plainText);
        System.out.println("Message digest is now: "+msgDigest);
        //TODO: 4. generate message digest - DONE BELOW
        byte[]signature = encryptMessageDigestWithUserPrivateKey(msgDigest);
        System.out.println("Signature is now: "+signature);
        //TODO: 6. Attach the encrypted hash/ signature above to the encrypted file's name & update listOfFile- MISSING
        //todo: 7. Update the signature status of the file - DONE BELOW - this can be merged in step 6
        return updateSignitureStatusOfSignedFileInListOfFiles(fileName);
    }
    /**
     * Step 5: Encrypt hash value with user's private key
     * @param msgDigest
     * @return byte[] signature
     */
    private static byte[] encryptMessageDigestWithUserPrivateKey(byte[]msgDigest){
        PrivateKey userPrivateKey = getUserPrivateKey();
        byte[] signature = Signature.addSignature(msgDigest, userPrivateKey);
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
    private static byte[] generateMessageDigest(String plainText){
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
   private static boolean updateSignitureStatusOfSignedFileInListOfFiles(String unsignedFileName){
       JsonFileHandler jsonFileHandler = new JsonFileHandler();
       boolean operationStatus = jsonFileHandler.UpdateSignatureStatusToExistingEncryptedFile(unsignedFileName);
       return operationStatus;
   }
    /**
     * Get existing user private key from UserRSAKeyFile where user isLogIn property (see User model) is true
     * The keys are generated and saved in this file on successful user signup process
     * @return PrivateKey
     */
    private static PrivateKey getUserPrivateKey(){
        //read users from file and find the logged in user true
        List<User> listOfUsers = JsonFileHandler.ReadObjectsFromJsonFile_UserRSAKeyFile();
        Object userPrivateKey = null;
        for(int i=0;i<listOfUsers.size();i++){
            if(listOfUsers.get(i).isLoggedIn()){
                //retrieve the private and public keys
                userPrivateKey = listOfUsers.get(i).getPrivateKey();
            }
        }
        return (PrivateKey) userPrivateKey;
    }
}
