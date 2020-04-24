# Password manager, encryption and signing software

## The steps required to run this application are

1. Select JDK 11(preferably) in project structure
1. Download and javafx 11
1. Set up libraries
   1. go to Project Settings > Libraries
      1. add bcprov-ext-jdk15to18-164.jar
      1. add gson-2.8.5.jar
      1. add *lib* folder of the previously downloaded javafx
1. Set up project configuration
    1. add a new configuration
    1. for Main class section select the file Main
    1. for VM options write *--module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml* with PATH_TO_FX being your path to the *lib* folder. Fx. *--module-path C:\Users\c\Downloads\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml*
1. Run the app