# Password manager, encryption and signing software

## The steps required to run this application are

1. Select JDK 11(preferably) in project structure
1. Download and javafx 11
1. Run project by right clicking Main.java file and clicking Run

#### If encountering Project configuration problems:
1. Set up project configuration(if there is a problem)
    1. add a new configuration
    1. for Main class section select the file Main
    1. for VM options write *--module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml* with PATH_TO_FX being your path to the *lib* folder. Fx. *--module-path C:\Users\c\Downloads\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml*
1. Run the app