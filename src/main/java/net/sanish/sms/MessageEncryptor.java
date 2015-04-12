package net.sanish.sms;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Class To encrypt messages using Jasypt.
 */
public class MessageEncryptor {

    private BasicTextEncryptor encryptor; // Instance of Jasypt TextEncryptor

    /**
     * @param key - the key to use for encryption and decrption
     */
    public MessageEncryptor(String key) {
        encryptor = new BasicTextEncryptor();
        encryptor.setPassword(key);
    }

    /**
     * @param clearText the text to encrypt
     * @return the encrypted text
     */
    public String encrypt(String clearText) {
        return encryptor.encrypt(clearText);
    }

    /**
     * @param cipherText the text to encrypt
     * @return the decrypted text
     */
    public String decrypt(String cipherText) {
        return encryptor.decrypt(cipherText);
    }
}
