package Utils;
import Libraries.RSAKeys;
import Models.User;

import java.sql.SQLOutput;
import java.util.List;

public class UserAuthentication {

    public String CreateNewUser(String username, String password){
        JsonFileHandler jfh = new JsonFileHandler();
        List<User> usersList = jfh.ReadObjectsFromJsonFile_UserRSAKeyFile();
        Object[] signUpUserMessage = CreateNewUser(username, password, usersList);
        return (String) signUpUserMessage[1];
    }
    public boolean LogInUser(String userName){
        System.out.println("The username is still: "+userName);
        return UpdateCurrentUserLoginStatus(userName, true);
    }
    public boolean LogOutUser(String userName){
        return UpdateCurrentUserLoginStatus(userName, false);
    }
    /**
     * Method to change the existing user status value from user file to the resuested one
     *  - can be used for login and logout & will change the status if it is different than the existing one
     * @param userName
     * @param requestedStatus
     * @return boolean if status was changed or not.
     */
    private boolean UpdateCurrentUserLoginStatus(String userName, boolean requestedStatus){
        System.out.println("Username value in updatecurrentuserloginstatus "+userName);
        JsonFileHandler jfh = new JsonFileHandler();
        List<User> listOfAllSystemUsers = jfh.ReadObjectsFromJsonFile_UserRSAKeyFile();
        System.out.println("The listOfAllSystemUsers before for loop is: "+listOfAllSystemUsers.get(0).getUsername()+" with status "+listOfAllSystemUsers.get(0).isLoggedIn()+" "+listOfAllSystemUsers.get(1).getUsername()+" with status "+listOfAllSystemUsers.get(1).isLoggedIn());
        for (int i = 0 ; i < listOfAllSystemUsers.size(); i++){
            User userObj = listOfAllSystemUsers.get(i);
            if(userObj.getUsername().equals(userName) && requestedStatus != userObj.isLoggedIn()){
                System.out.println("The username which seems to be fit is: "+userObj.getUsername());
                userObj.setLoggedIn(requestedStatus);
                System.out.println("The state status on user which seems to be fit is: "+requestedStatus);
                System.out.println("The listOfAllSystemUsers is: "+listOfAllSystemUsers.get(0).getUsername()+" with status "+listOfAllSystemUsers.get(0).isLoggedIn()+" "+listOfAllSystemUsers.get(1).getUsername()+" with status "+listOfAllSystemUsers.get(1).isLoggedIn());
                jfh.WriteObjectsToJsonFile_UserRSAKeyFile(listOfAllSystemUsers);
                return true;
            }
        }
        return false;
    }
    private Object[] CreateNewUser(String username, String password, List<User> usersList){
        Object[] signUpStatus = new Object[2];
        int length = usersList.size();
        int count=0;
        for (int i = 0; i < length; i++){
            String userNameFromList = usersList.get(i).getUsername();
            if (userNameFromList.equals(username)){
                count++;
            }
        }
        if(count == 0){
            try{
                //create the user object with keys and username
                User newUserObject = new User();
                JsonFileHandler jfh = new JsonFileHandler();
                newUserObject.setUsername(username);
                newUserObject.setPrivateKey(RSAKeys.generate()[0]);
                newUserObject.setPublicKey(RSAKeys.generate()[1]);
                newUserObject.setLoggedIn(false);
                //record the new user in the user list and record that list in the file
                usersList.add(newUserObject);
                jfh.WriteObjectsToJsonFile_UserRSAKeyFile(usersList);
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
