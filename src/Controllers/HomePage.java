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
import java.security.SecureRandom;
import java.security.Security;
import java.util.ResourceBundle;
import Models.UserFiles;
import Utils.JsonFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomePage implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private Button encryptBtn;
    @FXML
    private Button decryptBtn;
    @FXML
    private TextArea showSecret;
    @FXML
    private TextField secretkeyInput;

    ObservableList<String> fileList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> comboBoxFileSelector, comboBox_unsignedFile, comboBox_checkSignatureValidation;
    @FXML private Label selectedFileLable;

    ObservableList<String> fileList = FXCollections.observableArrayList();
    ObservableList<String> encryptedFileList = FXCollections.observableArrayList();
    ObservableList<String> signedFileList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//        selectionModel.select(1);
        populateUIFileList();
        comboBoxFileSelector.setItems(fileList);
        comboBox_unsignedFile.setItems(encryptedFileList);
        comboBox_checkSignatureValidation.setItems(signedFileList);
    }

    public void getComboBoxItem (ActionEvent event) {
        selectedFileLable.setText(comboBoxFileSelector.getValue());
        System.out.println(comboBoxFileSelector.getValue());
        JsonFileHandler fh = new JsonFileHandler();

        for (UserFiles item : fh.ReadObjectsFromJsonFile()) {
            if(item.getName().equals(comboBoxFileSelector.getValue())){
                showSecret.setText(item.getSecret());
//                return;
            }
        }
    }
    public void addFileSignature (ActionEvent event) {
        System.out.println(comboBox_unsignedFile.getValue());
    }
    public void checkSignatureValidation(ActionEvent event){
        System.out.println(comboBox_checkSignatureValidation.getValue());
    }

    private void populateUIFileList(){
        String itemStatus;
        boolean itemSignatureStatus;
        JsonFileHandler jsFileHandler = new JsonFileHandler();
        List<UserFiles> files = jsFileHandler.ReadObjectsFromJsonFile();
        files.forEach(file -> fileList.add(file.getName()));
        for(int i = 0; i < files.size(); i++){
            itemStatus = files.get(i).getStatus();
            itemSignatureStatus = files.get(i).getSignedStatus();
            if (itemStatus.contentEquals("Encrypted")){
                if(itemSignatureStatus == false){
                    encryptedFileList.add(files.get(i).getName());
                }else{
                    signedFileList.add(files.get(i).getName());
                }
            }
        }
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
//                file.delete();
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
                this.decrypt(fileValue, file.getName());
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

//        SecureRandom random = new SecureRandom();
//        byte[] keyBytes = new byte[16];
//        random.nextBytes(keyBytes);


        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
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
        FileUtils.write(s + "/src/assets/" + encryptedFileName, output);

        // write to ListOfFIles
        JsonFileHandler fh = new JsonFileHandler();
        List<UserFiles> objs = fh.ReadObjectsFromJsonFile();
        UserFiles user = new UserFiles();
        user.setName(encryptedFileName);
        user.setSecret(Hex.toHexString(keyBytes));
        objs.add(user);
        fh.WriteObjectsToJsonFile(objs);
    }

    private void decrypt(String value, String fileName) throws Exception{
        Security.addProvider(new BouncyCastleProvider());

        byte[] keyBytes = Hex.decode(secretkeyInput.getText());

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

        // TODO
        String mainName = fileName.split("\\.")[0];
        System.out.println(mainName);
        // String outFile = dir + "/" + mainName + "." + "decrypted" + "." + "pdf"; Utils.FileUtils.write(outFile, output);

        System.out.println("Current relative path is: " + s);
        FileUtils.write(s + "/src/assets/"  + mainName + ".decrypted." + ivString + "." + "aes", output);
    }

}
