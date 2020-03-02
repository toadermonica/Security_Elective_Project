package Controllers;
        import Models.File;
        import Utils.JsonFileHandler;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.Label;
        import java.net.URL;
        import java.util.List;
        import java.util.ResourceBundle;

public class HomePage implements Initializable {

    ObservableList<String> fileList = FXCollections.observableArrayList();

    @FXML private ComboBox<String> comboBoxFileSelector;
    @FXML private Label selectedFileLable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateUIFileList();
        comboBoxFileSelector.setItems(fileList);
    }

    public void getComboBoxItem(ActionEvent event) {
        selectedFileLable.setText(comboBoxFileSelector.getValue());
    }

    private void populateUIFileList(){
        JsonFileHandler jsFileHandler = new JsonFileHandler();
        List<File> files = jsFileHandler.ReadObjectsFromJsonFile();
        files.forEach(file -> fileList.add(file.getName()));
    }
}
