package Controllers;

import Models.User;
import Utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import Models.UserFiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;

public class HomePage implements Initializable {
    @FXML private TextArea showSecret, decryptedText;
    @FXML private TextField secretkeyInput, secretkeyInputInputDigitalSignature;
    @FXML private ComboBox<String> comboBoxFileSelector, comboBox_unsignedFile, comboBox_checkSignatureValidation, combobox_selectFileUser;
    @FXML private Label decryptAlertLabel, encryptedLabel, selectedFileLabel, welcomeUserLabel, signFileErrorLabel, validateSignatureSuccessLabel, validateSignatureErrorLabel, signFileMainLabel;

    private ObservableList<String> fileList = FXCollections.observableArrayList();
    private ObservableList<String> encryptedFileList = FXCollections.observableArrayList();
    private ObservableList<String> signedFileList = FXCollections.observableArrayList();
    private ObservableList<String> userNameList = FXCollections.observableArrayList();
    private EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
    private FileUtils fileUtils = new FileUtils();
    private String fileOwnerUserName = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeUserLabel.setText(getLoggedInUserName());
        populateAllUIFileList();
        comboBoxFileSelector.setItems(fileList);
        comboBox_unsignedFile.setItems(encryptedFileList);
        comboBox_checkSignatureValidation.setItems(signedFileList);
        getListOfActiveUsers();
        combobox_selectFileUser.setItems(userNameList);

    }
    public void logoutBtnAction(ActionEvent event) throws IOException {
        Parent loginPageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/LoginPage.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(loginPageRoot);
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
        String signedFileName = comboBox_checkSignatureValidation.getValue();
        if(signedFileName == null || fileOwnerUserName == null){
            validateSignatureErrorLabel.setText("Please select valid file owners and file names");
            return;
        }
        DigitalSignatureProcessing dsp = new DigitalSignatureProcessing();
        boolean signatureValidity = dsp.verifyDigitalSignature(signedFileName, fileOwnerUserName);
        if(signatureValidity){
            validateSignatureErrorLabel.setText("");
            validateSignatureSuccessLabel.setText("This file has a valid signature!");
        }else{
            validateSignatureSuccessLabel.setText("");
            validateSignatureErrorLabel.setText("Something went wrong! Invalid signature!");
        }
    }
    public void selectFileUser(ActionEvent event) {
        fileOwnerUserName = combobox_selectFileUser.getValue();
    }

    private void populateAllUIFileList(){
        String itemStatus;
        boolean itemSignatureStatus;
        String fileName;
        JsonFileHandler jsFileHandler = new JsonFileHandler();
        List<UserFiles> files = jsFileHandler.ReadObjectsFromJsonFile_ListOfFiles();
        for(int i = 0; i < files.size(); i++){
            fileName = files.get(i).getName();
            fileList.add(fileName);
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
    private void getListOfActiveUsers(){
        JsonFileHandler jsFileHandler = new JsonFileHandler();
        List<User> listOfUsers = jsFileHandler.ReadObjectsFromJsonFile_UserRSAKeyFile();
        for(int i = 0; i<listOfUsers.size(); i++){
            if(!listOfUsers.get(i).getUsername().equals(welcomeUserLabel.getText())){
                userNameList.add(listOfUsers.get(i).getUsername());
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
                populateAllUIFileList();

                encryptedLabel.setText("Encrypted file successfully.");
                encryptedLabel.setTextFill(Color.WHITE);
                encryptedLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
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
            try {
                String output = encryptDecrypt.decrypt(file.getName(), secretkeyInput);
                decryptedText.setText(output);
            } catch (Exception e) {
                decryptAlertLabel.setText("Please check if you have provided the correct secret key.");
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
            if(userObj.isLoggedIn()){
                return userObj.getUsername();
            }
        }
        return null;
    }

    public void decryptFileDigitalSignature(ActionEvent event) {
        String plainText = null;
        String unsignedEncryptedFileName = comboBox_unsignedFile.getValue();
        String secretKey = secretkeyInputInputDigitalSignature.getText();
        if(secretKey == null || secretKey == "" || secretKey.trim().isEmpty()){
            signFileErrorLabel.setText("Please insert a valid secret!");
            return;
        }
        if(unsignedEncryptedFileName == null){
            signFileErrorLabel.setText("Please select a valid file!");
            return;
        }
        try {
            plainText = encryptDecrypt.decryptSignature(unsignedEncryptedFileName, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            signFileErrorLabel.setText("Having problems with file content - possible corrupt file!");
        }
        if(plainText==null){
            signFileErrorLabel.setText("Having problems with file content - possible corrupt file!");
            return;
        }
        DigitalSignatureProcessing dgs =  new DigitalSignatureProcessing();
        String [] digitalSignatureProcessMessages = dgs.processDigitalSignature(unsignedEncryptedFileName, plainText);
        if(digitalSignatureProcessMessages[0].equals("Success")){
            signFileMainLabel.setText(digitalSignatureProcessMessages[1]);
            signFileErrorLabel.setText("");
        }else{
            signFileMainLabel.setText("");
            signFileErrorLabel.setText(digitalSignatureProcessMessages[1]);
        }
    }
}
