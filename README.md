# Password manager, encryption and signing software

## Steps required to run this application are

1. Select JDK 11(preferably) in project structure
1. Install javafx 11
1. Run project by right clicking src/Main.java file and clicking Run
1. To run tests right click src/Tests/EncryptDecryptTest.java and click Run

## Steps to login, encrypt, decrypt, sign and more
1. Signup and login
    1. to login you first have to signup, choose an username and a complex password(fx. username: *Bob*, password: *Complexpassword49!*)
    1. then click login
1. Encrypt
    1. go to File Management view
    1. click Encrypt file button, go to source code directory then to Assets directory(src/Assets) and select a txt file or create a new one with text inside
1. Decrypt
    1. go to Password Manager view and select file you just encrypted
    1. select and copy secret key output
    1. go to File Management view again, paste the secret key you just copied into the input field and click Decrypt file button, select the encrypted file and you can see the decrypted plaintext on the right side
1. Sign and verify file
    1. you will need to have 2 users for this operation, so please create another user(Alice)
    1. log in as Bob and encrypt a file and copy the secret key and go to *Digital Signatures* view and paste it in the input field in the *Signing files* section and select the encrypted file desired then click *Sign file* button
    
    1. Now please login as Alice, go to *Digital Signature* view and from the *Validate file signature* section select file owner as Bob and the signed file to validate the signature
    
#### If encountering Project configuration problems:
1. Set up project configuration(if there is a problem)
    1. add a new configuration
    1. for Main class section select the file Main
    1. for VM options write *--module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml* with PATH_TO_FX being your path to the *lib* folder. Fx. *--module-path C:\Users\c\Downloads\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml*
1. Run the app