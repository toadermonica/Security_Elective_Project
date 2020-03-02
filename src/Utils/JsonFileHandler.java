package Utils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import Models.File;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonFileHandler {

    private String filePath = "src/Assets/Files";

    //TODO: this is for when we encrypted and now we add the name of the encrypted file to this list so it can be used in pass manager
    public void WriteObjectsToJsonFile(){

    }

    /**
     * Reading json objects from file to be used in password manager combobox
     */
    public List<File> ReadObjectsFromJsonFile(){
        List<Models.File> files = null;
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(filePath));

            // convert JSON array to list of users
            files = new Gson().fromJson(reader, new TypeToken<List<File>>() {}.getType());

            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return files;
    }

}
