package Utils;

import Libraries.Digests;
import Libraries.UserRSAKeys;
import Libraries.DigitalSignatures;
import Models.User;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.List;


public class DigitalSignatureProcessing {

    /**
     * Main function that processes the given data and generates a signature of the given file
     * saves the signature in the listoffiles json as a standalone object property
     * @param fileName
     * @param plainText
     * @return status of updating file signature
     */
    public String[] processDigitalSignature(String fileName, String plainText){
        String [] responseArray = new String[2];
        String errorStatus = "Fail";
        String successStatus = "Success";
        byte[]msgDigest = generateMessageDigest(plainText);
        if (msgDigest == null){
            responseArray[0] = errorStatus;
            responseArray[1] = "Could not process file content!";
            return responseArray;
        }
        byte[]signature = encryptMessageDigestWithUserPrivateKey(msgDigest);
        if(signature == null){
            responseArray[0] = errorStatus;
            responseArray[1] = "Could not encrypt the signature! Please try again!";
            return responseArray;
        }
        boolean isFileListUpdatedWithSignature = updateSignatureAndStatusOfFile(fileName, signature);
        if (!isFileListUpdatedWithSignature){
            responseArray[0] = errorStatus;
            responseArray[1] = "Appending the signature failed! Please repeat this process!";
            return responseArray;
        }
        responseArray[0] = successStatus;
        responseArray[1] = "Your signature was added successfully";
        return responseArray;
    }

    public boolean verifyDigitalSignature(String fileName, String UserNameOrFileSender){
        System.out.println("The  fileName and the UserNameOfFileSender are: "+ fileName + " "+UserNameOrFileSender);
        JsonFileHandler jfh = new JsonFileHandler();
        DigitalSignatures ds = new DigitalSignatures();
        boolean isValidSignature = ds.verifySignature(UserNameOrFileSender, fileName);
        return isValidSignature;
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
            return null;
        }
        return signature;
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
    /**
     * Step 3: Generate hash based on plain text of file
     * @param plainText
     * @return byte[] hash from file plain text
     */
    public byte[] generateMessageDigest(String plainText){
        if(plainText==null){
            return null;
        }
        Digests digestInstance = new Digests();
        byte[] plainTextToBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] hashValue = digestInstance.generateMessageDigest(plainTextToBytes);
        return hashValue;
    }
}
