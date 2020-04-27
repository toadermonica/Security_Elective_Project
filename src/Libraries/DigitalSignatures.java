package Libraries;

import Models.*;
import Utils.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.List;

public class DigitalSignatures {

    public byte[] addSignature(byte[]msgDigestInput, PrivateKey privateKey){
        try {
            java.security.Signature signature =
                    java.security.Signature.getInstance("SHA256withRSA", "BC");
            signature.initSign(privateKey);
            signature.update(msgDigestInput);
            return signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean verifySignature (String userName, String fileName){
        Boolean isValidSignature = false;
        byte[] byteSignature = null;
        String secretKey = null;
        String plainTextInput = null;
        UserRSAKeys ursakeys = new UserRSAKeys();
        PublicKey userPublicKey = ursakeys.computeUserPublicKey(userName);
        if(userPublicKey == null){
            return isValidSignature;
        }
        JsonFileHandler jfh = new JsonFileHandler();
        List<UserFiles> listOfFiles = jfh.ReadObjectsFromJsonFile_ListOfFiles();
        for(int i = 0; i< listOfFiles.size(); i++){
            if(listOfFiles.get(i).getName().equals(fileName)){
                byteSignature = listOfFiles.get(i).getSignature();
                secretKey = listOfFiles.get(i).getSecret();
            }
        }
        if(byteSignature == null || secretKey == null){
            return isValidSignature;
        }
        EncryptDecrypt encryptionAndDecryption = new EncryptDecrypt();
        try {
            plainTextInput = encryptionAndDecryption.decryptSignature(fileName, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return isValidSignature;
        }
        DigitalSignatureProcessing dsg = new DigitalSignatureProcessing();
        byte[]input = dsg.generateMessageDigest(plainTextInput);
        try {
            java.security.Signature verifier = Signature.getInstance("SHA256withRSA", "BC");
            verifier.initVerify(userPublicKey);
            verifier.update(input);
            isValidSignature = verifier.verify(byteSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return isValidSignature;
        }
        return isValidSignature;
    }


}
