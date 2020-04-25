package Controllers;

import Libraries.RSAKeys;
import Models.User;
import Utils.JsonFileHandler;
import Utils.UserAuthentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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

    public void logInBtnAction(ActionEvent actionEvent) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        Parent homePageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/HomePage.fxml"));
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        if (isValidUserLogIn())
        {
            scene.setRoot(homePageRoot);
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
        System.out.println("Signing up new user status is: "+ userSignUpStatus(username, password));

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
        return "Success";
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
