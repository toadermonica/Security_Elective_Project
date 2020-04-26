package Controllers;

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
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label loginAlertLabel;

    @FXML
    private Label passwordValidationLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void logInBtnAction(ActionEvent actionEvent) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {

        if (!isValidUserLogIn()){
            loginAlertLabel.setText("Your username or password is incorrect.");
            loginAlertLabel.setTextFill(Color.RED);
            return;
        }

        UserAuthentication userAuth = new UserAuthentication();
        userAuth.LogInUser(usernameField.getText());

        //Setting of parent and scene needs to happen after the auth lines above - do not change the order or it will bug :)
        Parent homePageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/HomePage.fxml"));
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        scene.setRoot(homePageRoot);
    }

    public void signUpBtnAction(ActionEvent actionEvent) throws NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String password = passwordField.getText();
        UserAuthentication ua = new UserAuthentication();
        if (ua.passwordValidation(password) == false){
            passwordValidationLabel.setText("Password must contain the following:\n" +
                    "  *A lower case letter must occur at least once\n" +
                    "  *An upper case letter must occur at least once\n" +
                    "  *A special character must occur at least once\n" +
                    "  *No whitespace allowed in the entire string\n" +
                    "  *At least 8 characters");
            return;
        }

        System.out.println("You clicked SIGNIN btn");
        System.out.println("Username signup is "+usernameField.getText());
        System.out.println("Password signup is "+passwordField.getText());
        //Once the user is signed up, generate the necessary  keys for user to have an easy time from here.
        //this means we need the username and the keys to be saved in UserRSAKeyFile - need to write to jsonfile
        try{
//            System.out.println("Signing up new user status is: "+ userSignUpStatus(username, password));
            userSignUpStatus(usernameField.getText(), passwordField.getText());
            loginAlertLabel.setText("You have successfully created an account, now you can login.");
            loginAlertLabel.setTextFill(Color.GREEN);

        }catch (Exception e){
            loginAlertLabel.setText("This username is already taken.");
        }
    }


    private boolean isValidUserLogIn() throws NoSuchProviderException, NoSuchAlgorithmException {

        System.out.println("Username is "+usernameField.getText());
        System.out.println("Password is "+passwordField.getText());

        if(usernameField.getText() == null || usernameField.getText().trim().isEmpty() || passwordField.getText() == null || passwordField.getText().trim().isEmpty()){
            System.out.println("Username is "+usernameField.getText());
            System.out.println("Password is "+passwordField.getText());
            return false;
        }

        boolean isValid;
        UserAuthentication uauth = new UserAuthentication();
        isValid = uauth.SignIn(usernameField.getText(), passwordField.getText());
        if(!isValid){
            System.out.println("Username or password invalid");
            return false;
        }

        return true;
    }

    private String userSignUpStatus(String username, String password) throws NoSuchProviderException, NoSuchAlgorithmException, UnsupportedEncodingException {
        UserAuthentication userAuth = new UserAuthentication();
        String newUserSignUp = userAuth.SignUpNewUser(username, password);
        if(newUserSignUp != null){
            return newUserSignUp;
        }
        return "Success";
    }
}
