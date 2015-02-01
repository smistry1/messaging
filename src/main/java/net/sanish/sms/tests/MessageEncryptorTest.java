package net.sanish.sms.tests;

import android.test.AndroidTestCase;

import net.sanish.sms.MessageEncryptor;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

/**
 * Created by sanish on 31/01/2015.
 */
public class MessageEncryptorTest extends AndroidTestCase {

    public void testWithValidKey() {
        MessageEncryptor encryptor = new MessageEncryptor("12345abc");
        String cipherText = encryptor.encrypt("THIS IS A TEST");

        try {
            String clear = encryptor.decrypt(cipherText); // Decrypt using same key
            assertTrue(clear.equals("THIS IS A TEST"));  // If no exception & string is the same test passed
        } catch(EncryptionOperationNotPossibleException e) {
            assertFalse(true);          // Exception, test failed
        }
    }


    public void testWithWrongKey() {
        MessageEncryptor encryptor = new MessageEncryptor("12345abc");
        String cipherText = encryptor.encrypt("THIS IS A TEST");
        MessageEncryptor encryptor2 = new MessageEncryptor("wrongkey");
        try {
            String clear = encryptor2.decrypt(cipherText); // Decrypt using wrong key
            assertFalse(true);   // If no exception test failed
        } catch(EncryptionOperationNotPossibleException e) {
            assertTrue(true);          // Exception, test passed
        }
    }
}
