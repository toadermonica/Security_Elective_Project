package Utils;

import Libraries.RSAKeys;
import Models.User;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Objects;

public class UserAuthentication {

    public static String CreateNewUser(String username, String password){
        List<User> usersList = JsonFileHandler.ReadObjectsFromJsonFile_UserRSAKeyFile();
        Object[] signUpUserMessage = CreateNewUser(username, password, usersList);
        return (String) signUpUserMessage[1];
    }
    private static Object[] CreateNewUser(String username, String password, List<User> usersList){
        Object[] signUpStatus = new Object[2];
        int count=0;
        for (int i = 0; i < usersList.size(); i++){
            String userNameFromList = usersList.get(i).getUsername();
            if (userNameFromList.equals(username)){
                count++;
            }
        }
        if(count == 0){
            try{
                //create the user object with keys and username
                User newUserObject = new User();
                newUserObject.setUsername(username);
                newUserObject.setPrivateKey(RSAKeys.generate()[0]);
                newUserObject.setPublicKey(RSAKeys.generate()[1]);
                //record the new user in the user list and record that list in the file
                usersList.add(newUserObject);
                JsonFileHandler.WriteObjectsToJsonFile_UserRSAKeyFile(usersList);
                signUpStatus[0] = 1;
            }catch (Exception e){
                System.out.println("Exception was triggered: "+ e);
                signUpStatus[0] = 0;
                signUpStatus[1] = "this action failed! Please try again in a while.";
            }
        }else{
            signUpStatus[0] = 0;
            signUpStatus[1] = "We are sorry, but you already have an account! Try logging in. ";
        }
        return signUpStatus;
    }
}
