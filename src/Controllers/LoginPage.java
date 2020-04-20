package Controllers;

import Utils.UserAuthentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {
    private String username;
    private String password;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void logInBtnAction(ActionEvent actionEvent) throws IOException {
        if (!isValidUserLogIn()){
            return;
        }
        UserAuthentication userAuth = new UserAuthentication();
        userAuth.LogInUser(usernameField.getText());

        //Setting of parent and scene needs to happen after the auth lines above - do not change the order or it will bug :)
        Parent homePageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/HomePage.fxml"));
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        scene.setRoot(homePageRoot);
    }

    public void signUpBtnAction(ActionEvent actionEvent) {
        System.out.println("You clicked SIGNIN btn");
        System.out.println("Username signup is "+usernameField.getText());
        System.out.println("Password signup is "+passwordField.getText());
        //Once the user is signed up, generate the necessary  keys for user to have an easy time from here.
        //this means we need the username and the keys to be saved in UserRSAKeyFile - need to write to jsonfile
        System.out.println("Signing up new user status is: "+ userSignUpStatus(usernameField.getText(), passwordField.getText()));

    }

    private boolean isValidUserLogIn(){
        System.out.println("Username is "+usernameField.getText());
        System.out.println("Password is "+passwordField.getText());
        //if invalid user for login
        if(usernameField.getText() == null || usernameField.getText().trim().isEmpty() || passwordField.getText() == null || passwordField.getText().trim().isEmpty()){
            return false;
        }
        return true;
    }

    private String userSignUpStatus(String username, String password){
        UserAuthentication userAuth = new UserAuthentication();
        String newUserSignUp = userAuth.CreateNewUser(username, password);
        if(newUserSignUp != null){
            return newUserSignUp;
        }
        return "Success";
    }
}
