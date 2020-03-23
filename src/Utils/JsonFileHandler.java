package Utils;

import Models.UserFiles;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonFileHandler {
    private String filePath = "src/Assets/ListOfFiles";

    public void WriteObjectsToJsonFile(List<UserFiles> userFileItem){
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(filePath));

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
    public List<UserFiles> ReadObjectsFromJsonFile(){
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
}
