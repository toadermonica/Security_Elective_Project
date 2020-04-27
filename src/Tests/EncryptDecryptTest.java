package Tests;
import Utils.EncryptDecrypt;
import Utils.UserAuthentication;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import static org.junit.jupiter.api.Assertions.*;

class EncryptDecryptTest {

    @Test
    void encrypt() throws Exception {
        EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
        String output = encryptDecrypt.encrypt("this is a test", "mock.txt");

        assertEquals(output.length(), 32);
    }

    @Test
    void decrypt() throws Exception {
        EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
        String output = encryptDecrypt.decrypt2("this is a test", "cats.encrypted.9f741fdb5d8845bdb48a94394e84f8a3.aes", "a8f936737610190e3b288f62685def28");

        assertEquals(output, "cat1 cat2 cat3");
    }

    @Test
    void checkPasswordValidation() {
        UserAuthentication ua = new UserAuthentication();
        boolean validPassword = ua.passwordValidation("Catcat123!");
        assertEquals(validPassword, true);

        boolean invalidPasswordNoSpecialSymbol = ua.passwordValidation("Catcat123");
        assertEquals(invalidPasswordNoSpecialSymbol, false);

        boolean invalidPasswordNoUpperCaseLetter = ua.passwordValidation("catcat123!");
        assertEquals(invalidPasswordNoUpperCaseLetter, false);

        boolean invalidPasswordWhitespace = ua.passwordValidation("Catcat 123!");
        assertEquals(invalidPasswordWhitespace, false);

        boolean invalidPasswordNot8Characters = ua.passwordValidation("Catca3!");
        assertEquals(invalidPasswordNot8Characters, false);
    }
}