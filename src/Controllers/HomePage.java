package Controllers;

import Utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.ResourceBundle;

public class HomePage implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private Button encryptBtn;
    @FXML
    private Button decryptBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//        selectionModel.select(1);
    }

    public void encryptFile(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if(file != null){

            String fileValue = readFile(file);
            System.out.println(fileValue);
            try {
                HomePage.encrypt(fileValue, file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decryptFile(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AES files (*.aes)", "*.aes");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if(file != null){

            String fileValue = readFile(file);
            System.out.println(fileValue);
            try {
                HomePage.decrypt(fileValue, file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String readFile(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        return stringBuffer.toString();
    }


    private static void encrypt(String value, String fileName) throws Exception{
        String fileNameFormatted = fileName.substring(0, fileName.lastIndexOf('.'));
        System.out.println("file name is " + fileNameFormatted);
        Security.addProvider(new BouncyCastleProvider());

        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
        String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
        byte[] iv = Hex.decode(ivString);
        byte[] input = value.getBytes();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        System.out.println("input: " + new String(input));

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] output = cipher.doFinal(input);

        System.out.println("encrypted: " + Hex.toHexString(output));

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        FileUtils.write(s + "/src/assets/" + fileNameFormatted + "." + ivString + "." + "aes", output);
    }

    private static void decrypt(String value, String fileName) throws Exception{
        Security.addProvider(new BouncyCastleProvider());

        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
        String ivString = "9f741fdb5d8845bdb48a94394e84f8a3";
        byte[] iv = Hex.decode(ivString);

        // reading
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        String inFile = s + "/src/assets/" + fileName;
        byte[] input = FileUtils.readAllBytes(inFile);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        // TODO String ivString = getIV(fileName); // read the IV byte[] iv = Hex.decode(ivString);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] output = cipher.doFinal(input);
        System.out.println("OUTPUT: " + new String(output));
        // TODO String mainName = fileName.split(“[.]”)[0];
        // String outFile = dir + "/" + mainName + "." + "decrypted" + "." + "pdf"; Utils.FileUtils.write(outFile, output);
    }
}
