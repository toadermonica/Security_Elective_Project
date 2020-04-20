package Utils;

import Models.User;
import Models.UserFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonFileHandler {
    private String filePath = "src/Assets/ListOfFiles";
    private static String userFilePath = "src/CommonAssets/UserRSAKeyFile";

   public boolean AddEncryptedFileToList(UserFiles userFileItem){
       boolean isNewFile = true;
       int itemPositionInList = -1;
       // read the format of the file as a list -- this list will be updated with new writes
       List<UserFiles> currentItemsInJsonFile = ReadObjectsFromJsonFile_ListOfFiles();
       for(int i=0; i<currentItemsInJsonFile.size(); i++){
           if(currentItemsInJsonFile.get(i).getName().contentEquals(userFileItem.getName())){
               isNewFile = false;
           }
       }
       // if the file does not exist in the list of files
       if (isNewFile){
           // add the file
           currentItemsInJsonFile.add(userFileItem);
           WriteObjectsToJsonFile_ListOfFiles(currentItemsInJsonFile);
       }
       return isNewFile;
   }

    /**
     * Updating the signiture status of a file - use for successfully updating the current listOfFile & the list of signed files
     * @param userFileItemName
     */
   public boolean UpdateSignatureStatusToExistingEncryptedFile(String userFileItemName){
       boolean isUpdated = false;
       // read the format of the file as a list -- this list will be updated with new writes
       List<UserFiles> currentItemsInJsonFile = ReadObjectsFromJsonFile_ListOfFiles();
       for(int i=0; i<currentItemsInJsonFile.size(); i++){
           String currentItemInJsonFileName = currentItemsInJsonFile.get(i).getName();
           boolean currentIntemInJsonFileSignitureStatus = currentItemsInJsonFile.get(i).getSignedStatus();
           //if the name of the file matches and the signature status is false then proceed and update the signature status
           if(currentItemInJsonFileName.contentEquals(userFileItemName) && !currentIntemInJsonFileSignitureStatus){
               currentItemsInJsonFile.get(i).setSignedStatus(true);
               isUpdated = true;
           }
       }
       if(isUpdated){
           WriteObjectsToJsonFile_ListOfFiles(currentItemsInJsonFile);
       }
       System.out.println("Boolean return value is now: "+isUpdated);
       return isUpdated;

   }


    private void WriteObjectsToJsonFile_ListOfFiles(List<UserFiles> currentItemsInJsonFile){
        try {

            // create Gson instance
            Gson gson = new Gson();

            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(filePath));

            // convert user object to JSON file
            gson.toJson(currentItemsInJsonFile, writer);

            // close writer
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reading json objects from file to be used in password manager combobox
     */
    public List<UserFiles> ReadObjectsFromJsonFile_ListOfFiles(){
        List<UserFiles> files = null;
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(filePath));

            // convert JSON array to list of users
            files = new Gson().fromJson(reader, new TypeToken<List<UserFiles>>() {}.getType());

            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return files;
    }
    public static void WriteObjectsToJsonFile_UserRSAKeyFile(List<User> userFileItem){
        try {

            GsonBuilder gsonBuilder = new GsonBuilder();
            // create Gson instance
            Gson gson = gsonBuilder.serializeSpecialFloatingPointValues().create();

            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(userFilePath));

            // convert user object to JSON file
            gson.toJson(userFileItem, writer);

            // close writer
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reading json objects from file to be used in password manager combobox
     */
    public static List<User> ReadObjectsFromJsonFile_UserRSAKeyFile(){
        List<User> files = null;
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(userFilePath));

            // convert JSON array to list of users
            files = new Gson().fromJson(reader, new TypeToken<List<User>>() {}.getType());

            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return files;
    }

    /**
     * Simple file reader function
     */
    public String readFile(File file){
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


}
