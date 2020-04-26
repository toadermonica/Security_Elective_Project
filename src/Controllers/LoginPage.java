package Controllers;

import Libraries.RSAKeys;
import Models.User;
import Utils.JsonFileHandler;
import Utils.UserAuthentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.ResourceBundle;


public class LoginPage implements Initializable {
    private String username;
    private String password;

    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label loginAlertLabel;


    public void logInBtnAction(ActionEvent actionEvent) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        Parent homePageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/HomePage.fxml"));
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        if (isValidUserLogIn())
        {
            scene.setRoot(homePageRoot);
        }
        else {
            loginAlertLabel.setText("Your username or password is incorrect.");
            loginAlertLabel.setTextFill(Color.WHITE);
            loginAlertLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public void signUpBtnAction(ActionEvent actionEvent) throws NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("You clicked SIGNIN btn");
        username = usernameField.getText();//user_name.getText() == null || user_name.getText().trim().isEmpty()
        password = passwordField.getText();
        System.out.println("Username signup is "+username);
        System.out.println("Password signup is "+password);
        //Once the user is signed up, generate the necessary  keys for user to have an easy time from here.
        //this means we need the username and the keys to be saved in UserRSAKeyFile - need to write to jsonfile
        try{
//            System.out.println("Signing up new user status is: "+ userSignUpStatus(username, password));
            userSignUpStatus(username, password);
            loginAlertLabel.setText("You have successfully created an account, now you can login.");
            loginAlertLabel.setTextFill(Color.WHITE);
            loginAlertLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }catch (Exception e){
            loginAlertLabel.setText("This username is already taken.");
            loginAlertLabel.setTextFill(Color.WHITE);
            loginAlertLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }


    }

    private boolean isValidUserLogIn() throws NoSuchProviderException, NoSuchAlgorithmException {

        username = usernameField.getText();//user_name.getText() == null || user_name.getText().trim().isEmpty()
        password = passwordField.getText();
        System.out.println("Username is "+username);
        System.out.println("Password is "+password);

        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()){
            System.out.println("Username is "+username);
            System.out.println("Password is "+password);
            return false;
        }

        boolean isValid = UserAuthentication.SignIn(username, password);
        if(!isValid){
            System.out.println("Username or password invalid");
            return false;
        }

        return true;
    }

    private String userSignUpStatus(String username, String password) throws NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String newUserSignUp = UserAuthentication.CreateNewUser(username, password);
        if(newUserSignUp != null){
            return newUserSignUp;
        }
        else{
            System.out.println(32489984);
        }
        return "Success";
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
