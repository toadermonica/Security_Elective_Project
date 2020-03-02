package Controllers;

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
import java.net.URL;
import java.util.ResourceBundle;


public class LoginPage implements Initializable {
    private String username;
    private String password;

    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;

    public void logInBtnAction(ActionEvent actionEvent) throws IOException {
        Parent homePageRoot = FXMLLoader.load(getClass().getClassLoader().getResource("Views/HomePage.fxml"));
        Scene scene = ((Node) actionEvent.getSource()).getScene();
        if (isValidUserLogIn())
        {
            scene.setRoot(homePageRoot);
        }
    }

    public void signUpBtnAction(ActionEvent actionEvent) {
        System.out.println("You clicked SIGNIN btn");

    }

    private boolean isValidUserLogIn(){

        username = usernameField.getText();//user_name.getText() == null || user_name.getText().trim().isEmpty()
        password = passwordField.getText();
        System.out.println("Username is "+username);
        System.out.println("Password is "+password);

        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()){
            System.out.println("Username is "+username);
            System.out.println("Password is "+password);
            return false;
        }

        return true;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
