package Tests;
import Utils.EncryptDecrypt;
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
        String output = encryptDecrypt.decrypt2("this is a test", "cats.encrypted.9f741fdb5d8845bdb48a94394e84f8a3.aes", "e6686017a211a5897b6efdccc97d0e66");

        assertEquals(output, "cat1 cat2 cat3");
    }
}