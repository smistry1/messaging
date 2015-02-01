package net.sanish.sms;

import org.jasypt.util.text.BasicTextEncryptor;

public class MessageEncryptor {

    private BasicTextEncryptor encryptor; // Instance of Jasypt TextEncryptor

    public MessageEncryptor(String key) {
        encryptor = new BasicTextEncryptor();
        encryptor.setPassword(key);
    }

    public String encrypt(String clearText) {
        return encryptor.encrypt(clearText);
    }

    public String decrypt(String cipherText) {
        return encryptor.decrypt(cipherText);
    }
}
