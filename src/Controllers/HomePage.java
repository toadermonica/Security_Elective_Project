package Controllers;

import Models.User;
import Utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;

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
    @FXML
    private ComboBox<String> comboBoxFileSelector, comboBox_unsignedFile, comboBox_checkSignatureValidation;
    @FXML private Label selectedFileLabel;
    @FXML private Label welcomeUserLabel;
    @FXML private TextField secretkeyInputInputDigitalSignature;

    ObservableList<String> fileList = FXCollections.observableArrayList();
    ObservableList<String> encryptedFileList = FXCollections.observableArrayList();
    ObservableList<String> signedFileList = FXCollections.observableArrayList();
    EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
    FileUtils fileUtils = new FileUtils();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//        selectionModel.select(1);
        welcomeUserLabel.setText(getLoggedInUserName());
        populateAllUIFileList();
        comboBoxFileSelector.setItems(fileList);
        comboBox_unsignedFile.setItems(encryptedFileList);
        comboBox_checkSignatureValidation.setItems(signedFileList);
    }

    public void comboBoxEncryptedFileList(ActionEvent event) {
        selectedFileLabel.setText(comboBoxFileSelector.getValue());
        System.out.println(comboBoxFileSelector.getValue());
        JsonFileHandler fh = new JsonFileHandler();

        for (UserFiles item : fh.ReadObjectsFromJsonFile_ListOfFiles()) {
            if(item.getName().equals(comboBoxFileSelector.getValue())){
                showSecret.setText(item.getSecret());
            }
        }
    }
    public void checkSignatureValidation(ActionEvent event){
        System.out.println(comboBox_checkSignatureValidation.getValue());
    }

    private void populateAllUIFileList(){
        String itemStatus;
        boolean itemSignatureStatus;
        JsonFileHandler jsFileHandler = new JsonFileHandler();
        List<UserFiles> files = jsFileHandler.ReadObjectsFromJsonFile_ListOfFiles();
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

            String fileValue = fileUtils.readFile(file);
            System.out.println(fileValue);
            try {
                encryptDecrypt.encrypt(fileValue, file.getName());
                // NOTE uncomment this in production
//                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decryptFile(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File defaultDirectory = new File("src/Assets");
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(defaultDirectory);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AES files (*.aes)", "*.aes");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if(file != null){

            String fileValue = fileUtils.readFile(file);
            System.out.println(fileValue);
            try {
                encryptDecrypt.decrypt(file.getName(), secretkeyInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void userLogout(ActionEvent event) throws IOException {
        System.out.println("User to be logged out: "+welcomeUserLabel.getText());
        if(welcomeUserLabel.getText() == null){
            return;
        }
        UserAuthentication userAuth = new UserAuthentication();
        userAuth.LogOutUser(welcomeUserLabel.getText());
        Parent logInPageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/LogInPage.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(logInPageRoot);
    }
    private String getLoggedInUserName(){
        JsonFileHandler jfh = new JsonFileHandler();
        List<User> listOfAllSystemUsers = jfh.ReadObjectsFromJsonFile_UserRSAKeyFile();
        for (int i = 0 ; i < listOfAllSystemUsers.size(); i++){
            User userObj = listOfAllSystemUsers.get(i);
            System.out.println("This is the logged in user: "+userObj.isLoggedIn());
            if(userObj.isLoggedIn()){
                return userObj.getUsername();
            }
        }
        return null;
    }

    public void addFileSignature (ActionEvent event) {
        System.out.println(comboBox_unsignedFile.getValue());

    }

    public void decryptFileDigitalSignature(ActionEvent event) {
        //retrieve the name of the file selected in the dropdown list
        String unsignedEncryptedFileName = comboBox_unsignedFile.getValue();
        System.out.println("Unencrypted unsigned file name from combo box is: "+unsignedEncryptedFileName);
        //decrypt the text in the file and grab it in plain text variable

        String plainText = null;
        try {
            plainText = encryptDecrypt.CopyDecryptCristi(unsignedEncryptedFileName, secretkeyInputInputDigitalSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(plainText==null){
            //ToDO: add an error label that the user can see
            return;
        }
        // call on the digitalsignatureprocessing class
        DigitalSignatureProcessing dgs =  new DigitalSignatureProcessing();
        dgs.processDigitalSignature(unsignedEncryptedFileName, plainText);

    }
}
