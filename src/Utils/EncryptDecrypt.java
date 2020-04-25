package Utils;

import Models.UserFiles;
import javafx.scene.control.TextField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.Security;
import java.util.List;

public class EncryptDecrypt {
    public void encrypt(String value, String fileName) throws Exception{
        String fileNameFormatted = fileName.substring(0, fileName.lastIndexOf('.'));
        System.out.println("file name fileNameFormatted is " + fileNameFormatted);
        Security.addProvider(new BouncyCastleProvider());

        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[16];
        random.nextBytes(keyBytes);
        // byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
        byte[] iv = Hex.decode(ivString);
        byte[] input = value.getBytes();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

//        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        System.out.println("input: " + new String(input));

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] output = cipher.doFinal(input);

        System.out.println("encrypted: " + Hex.toHexString(output));

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        String encryptedFileName = fileNameFormatted + ".encrypted." + ivString + "." + "aes";
        FileUtils fileUtils = new FileUtils();
        fileUtils.write(s + "/src/assets/" + encryptedFileName, output);

        // write to ListOfFIles
        JsonFileHandler fh = new JsonFileHandler();
        List<UserFiles> objs = fh.ReadObjectsFromJsonFile();
        UserFiles user = new UserFiles();
        user.setName(encryptedFileName);
        user.setSecret(Hex.toHexString(keyBytes));
        user.setStatus("Encrypted");
        objs.add(user);
        fh.WriteObjectsToJsonFile(objs);
    }

    public void decrypt(String fileName, TextField secretkeyInput) throws Exception{
        System.out.println("Cristi's file name looks like this: "+fileName);
        Security.addProvider(new BouncyCastleProvider());

        byte[] keyBytes = Hex.decode(secretkeyInput.getText());

        String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
        byte[] iv = Hex.decode(ivString);

        // reading
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        String inFile = s + "/src/assets/" + fileName;
        FileUtils fileUtils = new FileUtils();
        byte[] input = fileUtils.readAllBytes(inFile);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        // TODO String ivString = getIV(fileName); // read the IV byte[] iv = Hex.decode(ivString);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] output = cipher.doFinal(input);
        System.out.println("OUTPUT: " + new String(output));

        // TODO
        String mainName = fileName.split("\\.")[0];
        System.out.println(mainName);
        // String outFile = dir + "/" + mainName + "." + "decrypted" + "." + "pdf"; Utils.FileUtils.write(outFile, output);

        System.out.println("Current relative path is: " + s);
        fileUtils.write(s + "/src/assets/"  + mainName + ".decrypted." + ivString + "." + "aes", output);
    }
    public String CopyDecryptCristi(String fileName, String secretkeyInput) throws Exception{
        System.out.println("Cristi's file name looks like this: "+fileName);
        System.out.println("secretkeyInput : "+secretkeyInput);

        Security.addProvider(new BouncyCastleProvider());

        byte[] keyBytes = Hex.decode(secretkeyInput);

        String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
        byte[] iv = Hex.decode(ivString);

        // reading
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        String inFile = s + "/src/assets/" + fileName;
        FileUtils fileUtils = new FileUtils();
        byte[] input = fileUtils.readAllBytes(inFile);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        // TODO String ivString = getIV(fileName); // read the IV byte[] iv = Hex.decode(ivString);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] output = cipher.doFinal(input);
        String planText = new String(output);
        System.out.println("OUTPUT: " + planText);
        return planText;
    }

}
